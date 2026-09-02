package com.setjy.practiceapp.presentation.model

import androidx.compose.runtime.Immutable
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

@Immutable
data class MessageUI(
    val userId: Int,
    val messageId: Int,
    val avatarUrl: String?,
    val username: String?,
    val message: String,
    val timestamp: String,
    val streamName: String,
    val topicName: String,
    val reactions: List<EmojiUI> = listOf(),
    val isOutgoingMessage: Boolean,
    override val uid: Int = messageId,
) : ViewTyped, Item