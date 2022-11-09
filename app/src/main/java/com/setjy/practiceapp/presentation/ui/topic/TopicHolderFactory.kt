package com.setjy.practiceapp.presentation.ui.topic

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.recycler.base.BaseViewHolder
import com.setjy.practiceapp.presentation.base.recycler.base.HolderFactory
import com.setjy.practiceapp.presentation.ui.topic.holder.IncomingMessageViewHolder
import com.setjy.practiceapp.presentation.ui.topic.holder.OutgoingMessageViewHolder

class TopicHolderFactory(
    private val onEmojiClickAction: (messageId: Int, emojiName: String, emojiCode: String) -> Unit,
    private val onAddEmojiClickAction: (messageId: Int) -> Unit
) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_msg_incoming -> IncomingMessageViewHolder(view).apply {
                view.setOnLongClickListener {
                    currentMessageId?.let(onAddEmojiClickAction)
                    true
                }
                onEmojiClick = onEmojiClickAction
                onAddEmojiClick = onAddEmojiClickAction
            }
            R.layout.item_msg_outgoing -> OutgoingMessageViewHolder(view).apply {
                view.setOnLongClickListener {
                    currentMessageId?.let(onAddEmojiClickAction)
                    true
                }
                onEmojiClick = onEmojiClickAction
                onAddEmojiClick = onAddEmojiClickAction
            }
            else -> null
        }
    }
}