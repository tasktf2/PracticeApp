package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.model.EmojiUI
import com.setjy.practiceapp.presentation.model.MessageUI
import javax.inject.Inject

data class MessageWithReactionsDomain(
    val userId: Int,
    val messageId: Int,
    val avatarUrl: String?,
    val username: String?,
    val message: String,
    val timestamp: String,
    val streamName: String,
    val topicName: String,
    val isOutgoingMessage: Boolean,
    val reactions: List<ReactionDomain>
) : Model

class MessageMapper @Inject constructor(
    private val reactionMapper: @JvmSuppressWildcards DomainMapper<ReactionDomain, EmojiUI>
) :
    DomainMapper<MessageWithReactionsDomain, MessageUI> {
    override fun mapToPresentation(model: MessageWithReactionsDomain): MessageUI = MessageUI(
        userId = model.userId,
        messageId = model.messageId,
        avatarUrl = model.avatarUrl,
        username = model.username,
        message = model.message,
        timestamp = model.timestamp,
        streamName = model.streamName,
        topicName = model.topicName,
        reactions = model.reactions.map(reactionMapper::mapToPresentation),
        isOutgoingMessage = model.isOutgoingMessage
    )
}