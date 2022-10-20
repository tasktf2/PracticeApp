package com.setjy.practiceapp.data.database.dao

import androidx.room.*
import com.setjy.practiceapp.data.database.entity.MessageDB
import com.setjy.practiceapp.data.database.entity.MessageWithReactionsDB

@Dao
interface MessagesDao {
    @Transaction
    @Query("SELECT * FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    fun getMessages(streamName: String, topicName: String): List<MessageWithReactionsDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessages(messages: List<MessageDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(message: MessageDB)

    @Transaction
    @Delete
    fun deleteMessages(messages: List<MessageDB>)

    @Transaction
    @Query("DELETE FROM message WHERE stream_name=:streamName AND topic_name=:topicName")
    fun deleteAllMessages(streamName: String, topicName: String)
}
