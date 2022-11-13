package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.local.db.dao.EventStorage
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.local.storage.MessageStorage
import com.setjy.practiceapp.data.local.storage.ReactionStorage
import com.setjy.practiceapp.data.remote.api.EventsApi
import com.setjy.practiceapp.data.remote.response.*
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.EventRepo
import com.setjy.practiceapp.domain.repo.MessageRepo
import io.reactivex.rxjava3.core.Observable
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class EventRepoImpl(
    private val api: EventsApi,
    private val messageRepo: MessageRepo,
    private val messageStorage: MessageStorage,
    private val reactionStorage: ReactionStorage,
    private val messageEntityMapper: MessageWithReactionsEntityMapper,
    private val messageRemoteMapper: MessagesRemoteMapper,
    private val ownUserId: Int,
    private val eventStorage: EventStorage
) : EventRepo {

    override fun subscribeToEvents(): Observable<SendEventResponse> =
        api.registerEventQueue().retryWhen { observableThrowable ->
            observableThrowable.flatMap { error ->
                if (error is UnknownHostException) {
                    Observable.timer(10, TimeUnit.SECONDS)
                } else {
                    observableThrowable
                }
            }
        }

    override fun getEvents(
        streamName: String,
        topicName: String,
        queueId: String,
        lastEventId: Int
    ): Observable<List<MessageWithReactionsDomain>> =
        api.getEventsQueue(
            queueId = queueId,
            lastEventId = lastEventId
        ).filter { it.events.isNotEmpty() }
            .flatMap { eventResponse ->
                eventStorage.insertLastEventId(eventResponse.events.last().eventId)
                messageRepo.getLocalMessages(streamName, topicName)
                    .toObservable()
                    .map { messages ->
                        handleMessageEvents(eventResponse.events, messages)
                    }.map { messages ->
                        handleReactionEvents(eventResponse.events, messages)
                    }
            }
            .retry { throwable -> throwable is SocketTimeoutException }
            .retryWhen { observableThrowable ->
                observableThrowable.flatMap { error ->
                    if (error is UnknownHostException) {
                        Observable.timer(10, TimeUnit.SECONDS)
                    } else {
                        observableThrowable
                    }
                }
            }

    override fun getLastEventId(): Int = eventStorage.getLastEventId()

    private fun handleMessageEvents(
        events: List<GetEventRemote>,
        messages: List<MessageWithReactionsDomain>
    ): List<MessageWithReactionsDomain> = messages +
            events.filter { it.type == EventType.MESSAGE }
                .map { event ->
                    if (messages.size == MESSAGES_TO_SAVE) {
                        messageStorage.deleteMessages(
                            listOf(messageEntityMapper.mapToEntity(messages.first()).message)
                        )
                    }
                    messageStorage.insertMessage(messageRemoteMapper.mapToEntity(event.message).message)
                    messageRemoteMapper.mapToDomain(event.message)
                }.asReversed()


    private fun handleReactionEvents(
        events: List<GetEventRemote>,
        messages: List<MessageWithReactionsDomain>
    ) = events.flatMap { event ->
        messages.map { message ->
            when (message.messageId) {
                event.messageId -> {
                    when (event.operation) {
                        EventOperation.ADD -> {
                            reactionStorage.insertReaction(event.toReactionEntity())
                            message.copy(
                                reactions = message.reactions + listOf(
                                    event.toReactionDomain()
                                )
                            )
                        }
                        EventOperation.REMOVE -> {
                            reactionStorage.deleteReaction(event.toReactionEntity())
                            if (event.userId == ownUserId) {
                                message.copy(reactions = message.reactions.filterNot { reaction ->
                                    reaction.code == event.emojiCode && reaction.userId == event.userId
                                })
                            } else {
                                val reactions = message.reactions.toMutableList()
                                reactions.remove(reactions.find { emoji ->
                                    emoji.code == event.emojiCode && emoji.userId == event.userId
                                })
                                message.copy(reactions = reactions)
                            }
                        }
                    }
                }
                else -> {
                    message
                }
            }
        }
    }

    companion object {
        private const val MESSAGES_TO_SAVE = 50
    }
}