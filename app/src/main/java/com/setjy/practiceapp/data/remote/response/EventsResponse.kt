package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.local.model.ReactionEntity
import com.setjy.practiceapp.domain.model.ReactionDomain

data class SendEventResponse(
    @SerializedName("last_event_id") val lastEventId: Int,
    @SerializedName("queue_id") val queueId: String,
)

data class GetEventResponse(
    val events: List<GetEventRemote>,
    @SerializedName("queue_id") val queueId: String
)

data class GetEventRemote(
    @SerializedName("type") val type: EventType,
    @SerializedName("id") val eventId: Int,
    @SerializedName("message") val message: MessagesRemote,

    @SerializedName("op") val operation: EventOperation,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user") val user: UserResponse,
    @SerializedName("message_id") val messageId: Int,
    @SerializedName("emoji_name") val emojiName: String,
    @SerializedName("emoji_code") val emojiCode: String
) {
    fun toReactionEntity() =
        ReactionEntity(code = emojiCode, name = emojiName, userId = userId, messageId = messageId)

    fun toReactionDomain() =
        ReactionDomain(code = emojiCode, name = emojiName, userId = userId, messageId = messageId)
}

enum class EventOperation {
    @SerializedName("add")
    ADD,

    @SerializedName("remove")
    REMOVE
}

enum class EventType {
    @SerializedName("message")
    MESSAGE,

    @SerializedName("reaction")
    REACTION
}
