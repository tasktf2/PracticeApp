package com.setjy.practiceapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

@Entity(tableName = "message")
data class MessageDB(
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
    val isOutgoingMessage: Boolean,
)