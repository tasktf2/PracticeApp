package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.Constants
import com.setjy.practiceapp.data.local.pref.AppPreferences
import com.setjy.practiceapp.data.local.storage.MessageStorage
import com.setjy.practiceapp.data.local.storage.ReactionStorage
import com.setjy.practiceapp.data.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.remote.api.EventsApi
import com.setjy.practiceapp.data.remote.response.EventOperation
import com.setjy.practiceapp.data.remote.response.EventType
import com.setjy.practiceapp.data.remote.response.GetEventRemote
import com.setjy.practiceapp.data.remote.response.MessagesRemoteMapper
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.EventRepo
import io.reactivex.rxjava3.core.Observable
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class EventRepoImpl constructor(
    private val api: EventsApi,
    private val messageRepo: MessageRepoImpl,
    private val messageStorage: MessageStorage,
    private val reactionStorage: ReactionStorage,
    private val messageEntityMapper: MessageWithReactionsEntityMapper,
    private val messageRemoteMapper: MessagesRemoteMapper
) : EventRepo {

    override fun getEvents(
        streamName: String,
        topicName: String
    ): Observable<List<MessageWithReactionsDomain>> =
        api.registerEventQueue()
            .flatMap { registerResponse ->
                api.getEventsQueue(
                    queueId = registerResponse.queueId,
                    lastEventId = registerResponse.lastEventId
                ).filter { it.events.isNotEmpty() }
                    .flatMap { eventResponse ->
                        messageRepo.getLocalMessages(streamName, topicName).toObservable()
                            .map { messages ->
                                handleMessageEvents(eventResponse.events, messages)
                            }.map { messages ->
                                handleReactionEvents(eventResponse.events, messages)
                            }
                    }
            }.retryWhen { observableThrowable ->
                observableThrowable.flatMap { error ->
                    if (error is UnknownHostException) {
                        Observable.timer(10, TimeUnit.SECONDS)
                    } else {
                        observableThrowable
                    }
                }
            }

    private fun handleMessageEvents(
        events: List<GetEventRemote>,
        messages: List<MessageWithReactionsDomain>
    ) =
        events.filter { it.type == EventType.MESSAGE }
            .map { event ->
                if (messages.size == Constants.MESSAGES_TO_SAVE) {
                    messageStorage.deleteMessages(
                        listOf(
                            messageEntityMapper.mapToEntity(
                                messages.last()
                            ).message
                        )
                    )
                }
                messageStorage.insertMessage(messageRemoteMapper.mapToEntity(event.message).message)
                messageRemoteMapper.mapToDomain(event.message)
            }.asReversed() + messages


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
                            if (event.userId == AppPreferences().getOwnUserId()) {
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
}