package com.setjy.practiceapp.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.setjy.practiceapp.data.base.EntityMapper
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.model.ReactionDomain
import javax.inject.Inject

data class MessageWithReactionsEntity(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "message_id"
    ) val reactions: List<ReactionEntity>
) : ModelEntity

class MessageWithReactionsEntityMapper @Inject constructor(private val reactionsEntityMapper: EntityMapper<ReactionEntity, ReactionDomain>) :
    EntityMapper<MessageWithReactionsEntity, MessageWithReactionsDomain> {

    override fun mapToDomain(entity: MessageWithReactionsEntity): MessageWithReactionsDomain =
        MessageWithReactionsDomain(
            userId = entity.message.userId,
            messageId = entity.message.messageId,
            avatarUrl = entity.message.avatarUrl,
            username = entity.message.username,
            message = entity.message.message,
            timestamp = entity.message.timestamp,
            streamName = entity.message.streamName,
            topicName = entity.message.topicName,
            isOutgoingMessage = entity.message.isOutgoingMessage,
            reactions = entity.reactions.map(reactionsEntityMapper::mapToDomain)
        )

    override fun mapToEntity(model: MessageWithReactionsDomain): MessageWithReactionsEntity =
        MessageWithReactionsEntity(
            message = MessageEntity(
                userId = model.userId,
                messageId = model.messageId,
                avatarUrl = model.avatarUrl,
                username = model.username,
                message = model.message,
                timestamp = model.timestamp,
                streamName = model.streamName,
                topicName = model.topicName,
                isOutgoingMessage = model.isOutgoingMessage
            ),
            reactions = model.reactions.map(reactionsEntityMapper::mapToEntity)
        )
}