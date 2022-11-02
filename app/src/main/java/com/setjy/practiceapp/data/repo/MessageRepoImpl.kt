package com.setjy.practiceapp.data.repo

import com.google.gson.Gson
import com.setjy.practiceapp.data.Constants
import com.setjy.practiceapp.data.local.storage.MessageStorage
import com.setjy.practiceapp.data.local.storage.ReactionStorage
import com.setjy.practiceapp.data.model.MessageWithReactionsEntityMapper
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

class MessageRepoImpl constructor(
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
        .map { entities -> entities.map { entityMapper.mapToDomain(it) } }

    override fun getRemoteMessages(
        streamName: String,
        topicName: String,
        anchor: String
    ): Observable<List<MessageWithReactionsDomain>> =
        messageStorage.getMessages(streamName, topicName).flatMapObservable { localMessages ->
            api.getMessages(
                anchor = anchor,
                numBefore = Constants.MESSAGES_TO_LOAD_BEFORE,
                numAfter = Constants.MESSAGES_TO_LOAD_AFTER,
                narrow = Gson().toJson(
                    listOf(
                        Narrow(operator = Constants.NARROW_STREAM, operand = streamName),
                        Narrow(operator = Constants.NARROW_TOPIC, operand = topicName)
                    )
                )
            )
                .map { response -> response.messages.map { remoteMapper.mapToDomain(it) } }
                .doAfterNext { messagesDomain ->
                    if (anchor != Constants.ANCHOR_NEWEST && localMessages.size < Constants.MESSAGES_TO_SAVE) {
                        val messagesToInsert =
                            messagesDomain.filterNot { it.messageId == anchor.toInt() }
                                .takeLast(Constants.MESSAGES_TO_SAVE - localMessages.size)

                        messageStorage.insertMessages(messagesToInsert
                            .map { entityMapper.mapToEntity(it).message })
                        reactionStorage.insertReactions(messagesToInsert
                            .map { entityMapper.mapToEntity(it) })

                    }
                    if (anchor == Constants.ANCHOR_NEWEST) {
                        messageStorage.deleteAllMessages(streamName, topicName)
                        messageStorage.insertMessages(messagesDomain
                            .map { entityMapper.mapToEntity(it).message })
                        reactionStorage.insertReactions(messagesDomain
                            .map { entityMapper.mapToEntity(it) })
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

    override fun getMessagesOnLaunch(
        streamName: String,
        topicName: String
    ): Flowable<List<MessageWithReactionsDomain>> =
        Single.concatArrayEager(
            getLocalMessages(streamName, topicName),
            Single.fromObservable(getRemoteMessages(streamName, topicName, Constants.ANCHOR_NEWEST))
        )

    override fun sendMessage(streamName: String, topicName: String, message: String): Completable =
        api.sendMessage(streamName = streamName, topicName = topicName, message = message)
}