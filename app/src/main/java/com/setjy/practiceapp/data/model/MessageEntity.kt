package com.setjy.practiceapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.data.base.ModelEntity

@Entity(tableName = "message")
data class MessageEntity(
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId: Int,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    @ColumnInfo(name = "username")
    val username: String?,
    @ColumnInfo(name = "message")
    val message: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: String,
    @ColumnInfo(name = "stream_name")
    val streamName: String,
    @ColumnInfo(name = "topic_name")
    val topicName: String,
    @ColumnInfo(name = "is_outgoing_message")
    val isOutgoingMessage: Boolean
)