package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

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
    val isFound: Boolean = false,
    val isOutgoingMessage: Boolean,
    override val viewType: Int = if (isOutgoingMessage) {
        R.layout.item_msg_outgoing
    } else {
        R.layout.item_msg_incoming
    }
) : ViewTyped