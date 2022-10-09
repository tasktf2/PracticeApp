package com.setjy.practiceapp.data.database

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.data.network.EmojiRemote
import com.setjy.practiceapp.recycler.items.*
import io.reactivex.rxjava3.core.Completable
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

    private val usersDao = database.usersDao()

    fun getAllStreams(): Single<List<StreamItemUI>> =
        Single.fromCallable { streamDao.getAllStreams() }

    fun getSubscribedStreams(): Single<List<StreamItemUI>> =
        Single.fromCallable { streamDao.getSubscribedStreams() }

    fun insertAllStreams(streams: List<StreamItemUI>): Single<Unit> =
        Single.fromCallable { streamDao.insertAllStreams(streams) }

    fun getTopicsByStreamId(streamId: Int): Single<List<TopicItemUI>> =
        Single.fromCallable { topicDao.getTopicsByStreamId(streamId) }

    fun insertTopics(topics: List<TopicItemUI>): Single<Unit> =
        Single.fromCallable { topicDao.insertTopics(topics) }

    fun getMessages(streamName: String, topicName: String): Single<List<MessageDB>> =
        Single.fromCallable { messagesDao.getMessages(streamName, topicName) }

    fun insertMessages(messages: List<MessageDB>): Completable =
        Completable.fromCallable { messagesDao.insertMessages(messages) }

    fun insertMessage(message: MessageDB): Completable =
        Completable.fromCallable { messagesDao.insertMessage(message) }

    fun deleteMessages(messages: List<MessageDB>): Completable =
        Completable.fromCallable { messagesDao.deleteMessages(messages) }

    fun getUser(): Single<List<UserItemUI>> =
        Single.fromCallable { usersDao.getUser() }

    fun insertUser(user: UserItemUI): Completable =
        Completable.fromCallable { usersDao.insertUser(user) }

    fun getReactionsByMessageId(messageId: Int): Single<List<EmojiUI>> =
        Single.fromCallable { reactionsDao.getReactionsByMessageId(messageId) }.map { emojiRemote ->
            emojiRemote.map {
                EmojiUI(
                    emojiName = it.name,
                    code = it.code,
                    isSelected = it.userId == Data.getUserOwnId()
                )
            }
        }

    fun insertReactions(reactions: List<EmojiUI>, messageId: Int, userId: Int): Single<Unit> =
        Single.fromCallable {
            reactionsDao.insertReactions(reactions.map {
                EmojiRemote(
                    code = it.code,
                    name = it.emojiName,
                    userId = userId,
                    messageId = messageId
                )
            })
        }
    fun insertReaction(reaction: EmojiUI, messageId: Int, userId: Int): Completable =
        Completable.fromCallable {
            reactionsDao.insertReaction(
                EmojiRemote(
                    code = reaction.code,
                    name = reaction.emojiName,
                    userId = userId,
                    messageId = messageId
                )
            )
        }

    fun deleteReactions(reactions: List<EmojiUI>, messageId: Int, userId: Int): Completable =
        Completable.fromCallable {
            reactionsDao.deleteReactions(reactions.map {
                EmojiRemote(
                    code = it.code,
                    name = it.emojiName,
                    userId = userId,
                    messageId = messageId
                )
            })
        }
    fun deleteReaction(reaction: EmojiUI, messageId: Int, userId: Int): Completable =
        Completable.fromCallable {
            reactionsDao.deleteReaction(
                EmojiRemote(
                    code = reaction.code,
                    name = reaction.emojiName,
                    userId = userId,
                    messageId = messageId
                )
            )
        }

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
