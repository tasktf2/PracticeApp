package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class IncomingMessageUI(
    val messageId: String,
    val avatar: Int,
    val username: String,
    val message: String,
    val reactions: List<EmojiUI>,
    override val viewType: Int = R.layout.item_msg_incoming,
) : ViewTyped