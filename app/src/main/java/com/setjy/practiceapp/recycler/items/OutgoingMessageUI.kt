package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.DEFAULT_USER_ID
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class OutgoingMessageUI(
    val messageId: String,
    val user_id: String = DEFAULT_USER_ID,
    val text: String,
    val reactions: List<EmojiUI>? = null,
    override val viewType: Int = R.layout.item_msg_outgoing
) : ViewTyped