package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class OutgoingMessageUI(
    val messageId: Int,
    val userId: Int,
    val message: String,
    val reactions: List<EmojiUI> = listOf(),
    val timestamp: String,
    val isFound: Boolean = false,
    override val viewType: Int = R.layout.item_msg_outgoing
) : ViewTyped