package com.setjy.practiceapp.data.local.db.dao

import androidx.room.*
import com.setjy.practiceapp.data.local.model.MessageEntity
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntity

@Dao
interface MessageDao {
    @Transaction
    @Query("SELECT * FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    fun getMessages(streamName: String, topicName: String): List<MessageWithReactionsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(message: MessageEntity)

    @Transaction
    @Delete
    fun deleteMessages(messages: List<MessageEntity>)

    @Transaction
    @Query("DELETE FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    fun deleteAllMessages(streamName: String, topicName: String)
}
