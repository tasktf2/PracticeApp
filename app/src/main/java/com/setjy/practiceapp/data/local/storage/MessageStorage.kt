package com.setjy.practiceapp.data.local.storage

import com.setjy.practiceapp.data.local.db.dao.MessageDao
import com.setjy.practiceapp.data.local.model.MessageEntity
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntity
import io.reactivex.rxjava3.core.Single

class MessageStorage(private val messageDao: MessageDao) {

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
}