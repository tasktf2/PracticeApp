package com.setjy.practiceapp.data.repo

import com.google.gson.Gson
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntityMapper
import com.setjy.practiceapp.data.local.storage.MessageStorage
import com.setjy.practiceapp.data.local.storage.ReactionStorage
import com.setjy.practiceapp.data.remote.api.MessageApi
import com.setjy.practiceapp.data.remote.response.MessagesRemoteMapper
import com.setjy.practiceapp.data.remote.response.Narrow
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.MessageRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class MessageRepoImpl(
    private val api: MessageApi,
    private val messageStorage: MessageStorage,
    private val reactionStorage: ReactionStorage,
    private val entityMapper: MessageWithReactionsEntityMapper,
    private val remoteMapper: MessagesRemoteMapper
) : MessageRepo {

    override fun getLocalMessages(
        streamName: String,
        topicName: String
    ): Single<List<MessageWithReactionsDomain>> = messageStorage.getMessages(streamName, topicName)
        .map { entities -> entities.map(entityMapper::mapToDomain) }

    override fun getRemoteMessages(
        streamName: String,
        topicName: String,
        anchor: String
    ): Observable<List<MessageWithReactionsDomain>> =
        messageStorage.getMessages(streamName, topicName).flatMapObservable { localMessages ->
            api.getMessages(
                anchor = anchor,
                numBefore = MESSAGES_TO_LOAD_BEFORE,
                numAfter = MESSAGES_TO_LOAD_AFTER,
                narrow = Gson().toJson(
                    listOf(
                        Narrow(operator = NARROW_STREAM, operand = streamName),
                        Narrow(operator = NARROW_TOPIC, operand = topicName)
                    )
                )
            )
                .map { response -> response.messages.map(remoteMapper::mapToDomain) }
                .doAfterNext { messagesDomain ->
                    if (anchor != ANCHOR_NEWEST && localMessages.size < MESSAGES_TO_SAVE) {
                        val messagesToInsert =
                            messagesDomain.filterNot { it.messageId == anchor.toInt() }
                                .takeLast(MESSAGES_TO_SAVE - localMessages.size)

                        messageStorage.insertMessages(
                            messagesToInsert.map { entityMapper.mapToEntity(it).message }
                        )
                        reactionStorage.insertReactions(
                            messagesToInsert.map(entityMapper::mapToEntity)
                        )
                    }
                    if (anchor == ANCHOR_NEWEST) {
                        messageStorage.deleteAllMessages(streamName, topicName)
                        messageStorage.insertMessages(messagesDomain
                            .map { entityMapper.mapToEntity(it).message })
                        reactionStorage.insertReactions(
                            messagesDomain.map(entityMapper::mapToEntity)
                        )
                    }
                }
                .retryWhen { observableThrowable ->
                    observableThrowable.flatMap { error ->
                        if (error is UnknownHostException) {
                            Observable.timer(10, TimeUnit.SECONDS)
                        } else {
                            observableThrowable
                        }
                    }
                }
        }

    override fun getNewestMessages(
        streamName: String,
        topicName: String
    ): Flowable<List<MessageWithReactionsDomain>> =
        Single.concatArrayEager(
            getLocalMessages(streamName, topicName),
            Single.fromObservable(getRemoteMessages(streamName, topicName, ANCHOR_NEWEST))
        )

    override fun sendMessage(streamName: String, topicName: String, message: String): Completable =
        api.sendMessage(streamName = streamName, topicName = topicName, message = message)

    companion object {
        private const val NARROW_STREAM = "stream"

        private const val NARROW_TOPIC = "topic"

        const val MESSAGES_TO_LOAD_BEFORE = 20

        const val MESSAGES_TO_LOAD_AFTER = 0

        const val ANCHOR_NEWEST = "newest"

        const val MESSAGES_TO_SAVE: Int = 50
    }
}