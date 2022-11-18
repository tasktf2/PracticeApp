package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.base.ModelRemote
import com.setjy.practiceapp.data.base.RemoteMapper
import com.setjy.practiceapp.data.local.model.MessageEntity
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntity
import com.setjy.practiceapp.di.module.topic.TopicModule
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.util.getMessageTimeStamp
import javax.inject.Inject
import javax.inject.Named

data class MessagesResponse(
    @SerializedName("messages") val messages: List<MessagesRemote>
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
    @SerializedName("recipient_id") val recipientId: Int, //topic id
) : ModelRemote

class MessagesRemoteMapper @Inject constructor(@Named(TopicModule.NAMED_USER_ID) private val ownUserId: Int) :
    RemoteMapper<MessagesRemote, MessageWithReactionsDomain, MessageWithReactionsEntity> {
    override fun mapToDomain(remote: MessagesRemote): MessageWithReactionsDomain =
        MessageWithReactionsDomain(
            userId = remote.senderId,
            messageId = remote.messageId,
            avatarUrl = remote.avatarUrl,
            username = remote.senderFullName,
            message = remote.content,
            timestamp = getMessageTimeStamp(remote.timestamp),
            streamName = remote.streamName,
            topicName = remote.topicName,
            isOutgoingMessage = remote.senderId == ownUserId,
            reactions = remote.reactions.map { it.toDomain(remote.messageId) }
        )

    override fun mapToEntity(remote: MessagesRemote): MessageWithReactionsEntity =
        MessageWithReactionsEntity(
            MessageEntity(
                userId = remote.senderId,
                messageId = remote.messageId,
                avatarUrl = remote.avatarUrl,
                username = remote.senderFullName,
                message = remote.content,
                timestamp = getMessageTimeStamp(remote.timestamp),
                streamName = remote.streamName,
                topicName = remote.topicName,
                isOutgoingMessage = remote.senderId == ownUserId,
            ),
            reactions = remote.reactions.map { it.toEntity(remote.messageId) }
        )
}