package com.setjy.practiceapp.data.local.storage

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.model.MessageEntity
import com.setjy.practiceapp.data.model.MessageWithReactionsEntity
import io.reactivex.rxjava3.core.Single

class MessageStorage constructor(context: Context) {
    private val database: ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        context.getString(R.string.database_name)
    ).build()

    private val messageDao = database.messageDao()

    fun getMessages(
        streamName: String,
        topicName: String
    ): Single<List<MessageWithReactionsEntity>> =
        Single.fromCallable { messageDao.getMessages(streamName, topicName) }

    fun insertMessages(messages: List<MessageEntity>) = messageDao.insertMessages(messages)

    fun insertMessage(message: MessageEntity) = messageDao.insertMessage(message)

    fun deleteMessages(messages: List<MessageEntity>) = messageDao.deleteMessages(messages)

    fun deleteAllMessages(streamName: String, topicName: String) =
        messageDao.deleteAllMessages(streamName, topicName)

    companion object {
        private var STORAGE_INSTANCE: MessageStorage? = null
        fun initialize(context: Context) {
            if (STORAGE_INSTANCE == null) STORAGE_INSTANCE = MessageStorage(context)
        }

        fun get(): MessageStorage {
            return STORAGE_INSTANCE
                ?: throw IllegalStateException("MessagesStorage must be initialized")
        }
    }
}