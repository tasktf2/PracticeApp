package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.DEFAULT_INCOMING_USER_ID
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class IncomingMessageUI(
    val userId: String = DEFAULT_INCOMING_USER_ID,
    val messageId: String,
    val avatar: Int,
    val username: String,
    val message: String,
    val reactions: List<EmojiUI> = listOf(),
    val isFound: Boolean = false,
    override val viewType: Int = R.layout.item_msg_incoming,
) : ViewTyped