package com.setjy.practiceapp.data.network.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.database.entity.MessageDB

data class MessagesResponse(
    val messages: List<MessagesRemote>
)

data class MessagesRemote(
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("content") val content: String,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("sender_full_name") val senderFullName: String,
    @SerializedName("id") val messageId: Int,
    @SerializedName("reactions") val reactions: List<ReactionsResponse>,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("timestamp") val timestamp: Long,

    @SerializedName("display_recipient") val streamName: String, //stream name (displayRecipient)
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("subject") val topicName: String, //topic name
    @SerializedName("recipient_id") val recipientId: Int, //topic id??? (for hashing)
)

data class ReactionsResponse(
    @SerializedName("emoji_code") val emojiCode: String,
    @SerializedName("emoji_name") val emojiName: String,
    @SerializedName("user_id") val userId: Int,
)

@Entity(
    tableName = "reaction",
    primaryKeys = ["message_id", "user_id", "emoji_code"],
    foreignKeys = [ForeignKey(
        entity = MessageDB::class,
        parentColumns = ["message_id"],
        childColumns = ["message_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class EmojiRemote(
    @ColumnInfo(name = "emoji_code")
    val code: String,
    @ColumnInfo(name = "emoji_name")
    val name: String,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "message_id")
    val messageId: Int
)

data class EmojiToggleResponse(
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: String?,
    @SerializedName("result") val result: String
)