package com.setjy.practiceapp.data.network

import com.google.gson.annotations.SerializedName

data class StreamsResponse(val streams: List<StreamsRemote>)

data class StreamsSubscribedResponse(val subscriptions: List<StreamsRemote>)

data class StreamsRemote(
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("name") val streamName: String,
    @SerializedName("color") val streamColor: String?
)

data class TopicsResponse(val topics: List<TopicsRemote>)

data class TopicsRemote(
    @SerializedName("max_id") val topicId: Int,
    @SerializedName("name") val topicName: String
)

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

data class EmojiRemote(
    val code: String,
    val name: String,
    val userId: Int
)

data class UsersResponse(val members: List<UsersRemote>)

data class UsersRemote(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("delivery_email") val userEmail: String,
    @SerializedName("timezone") val userTimeZone: String
)

data class UserResponse(
    @SerializedName("id") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
)

data class Narrow(
    @SerializedName("operator") val operator: String,
    @SerializedName("operand") val operand: String,
)

data class UserStatusPresence(
    val presence: UserStatusResponse
)

data class UserStatusResponse(
    @SerializedName("aggregated") val statusAndTimestamp: UserStatusRemote
)

data class UserStatusRemote(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("status") val status: String
)

data class EmojiToggleResponse(
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: String?,
    @SerializedName("result") val result: String
)

data class MessageSendResponse(
    @SerializedName("id") val messageId: Int,
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: String?,
    @SerializedName("result") val result: String
)

data class SendEventResponse(
    @SerializedName("last_event_id") val lastEventId: Int,
    @SerializedName("queue_id") val queueId: String,
)

data class GetEventResponse(
    val events: List<GetEventRemote>,
    @SerializedName("queue_id") val queueId: String
)

data class GetEventRemote(
    @SerializedName("type") val type: String,
    @SerializedName("id") val eventId: Int,
    @SerializedName("message") val message: MessagesRemote,

    @SerializedName("op") val operation: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("message_id") val messageId: Int,
    @SerializedName("emoji_name") val emojiName: String,
    @SerializedName("emoji_code") val emojiCode: String
)