package com.setjy.practiceapp.data.database

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.data.database.entity.*
import com.setjy.practiceapp.data.network.response.EmojiRemote
import com.setjy.practiceapp.data.network.response.MessagesRemote
import com.setjy.practiceapp.recycler.items.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class ZulipRepo private constructor(context: Context) {
    private val database: ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        "zulip_database"
    ).build()

    private val streamDao = database.streamDao()

    private val topicDao = database.topicDao()

    private val messagesDao = database.messagesDao()

    private val reactionsDao = database.reactionsDao()

    fun getStreams(isSubscribed: Boolean): Single<List<StreamItemDB>> =
        Single.fromCallable { streamDao.getStreams(isSubscribed) }

    fun insertAllStreams(streams: List<StreamItemDB>) = streamDao.insertAllStreams(streams)

    fun getTopicsByStreamId(streamId: Int): Single<List<TopicItemUI>> =
        Single.fromCallable { topicDao.getTopicsByStreamId(streamId) }

    fun insertTopics(topics: List<TopicItemUI>) = topicDao.insertTopics(topics)

    fun getMessages(streamName: String, topicName: String): Single<List<MessageWithReactionsDB>> =
        Single.fromCallable { messagesDao.getMessages(streamName, topicName) }

    fun insertMessages(messages: List<MessageDB>) = messagesDao.insertMessages(messages)

    fun insertMessage(message: MessageDB) = messagesDao.insertMessage(message)

    fun deleteMessages(messages: List<MessageDB>) = messagesDao.deleteMessages(messages)

    fun deleteAllMessages(streamName: String, topicName: String) =
        messagesDao.deleteAllMessages(streamName, topicName)

    fun insertReactions(messages: List<MessagesRemote>) = messages.forEach { message ->
        reactionsDao.insertReactions(
            message.reactions.map { reaction ->
                EmojiRemote(
                    code = reaction.emojiCode,
                    name = reaction.emojiName,
                    messageId = message.messageId,
                    userId = reaction.userId
                )
            })
    }

    fun insertReaction(reaction: EmojiUI, messageId: Int, userId: Int) =
        reactionsDao.insertReaction(
            EmojiRemote(
                code = reaction.code,
                name = reaction.emojiName,
                userId = userId,
                messageId = messageId
            )
        )

    fun deleteReaction(reaction: EmojiUI, messageId: Int, userId: Int) =
        reactionsDao.deleteReaction(
            EmojiRemote(
                code = reaction.code,
                name = reaction.emojiName,
                userId = userId,
                messageId = messageId
            )
        )

    companion object {
        private var REPO_INSTANCE: ZulipRepo? = null
        fun initialize(context: Context) {
            if (REPO_INSTANCE == null) REPO_INSTANCE = ZulipRepo(context)
        }

        fun get(): ZulipRepo {
            return REPO_INSTANCE
                ?: throw IllegalStateException("StreamRepository must be initialized")
        }

    }
}
