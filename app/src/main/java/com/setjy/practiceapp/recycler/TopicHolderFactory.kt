package com.setjy.practiceapp.recycler

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.holders.IncomingMessageViewHolder
import com.setjy.practiceapp.recycler.holders.OutgoingMessageViewHolder
import com.setjy.practiceapp.recycler.holders.TimeDividerViewHolder

class TopicHolderFactory(
    private val onEmojiClickAction: (messageId: Int, emojiName: String, emojiCode: String) -> Unit,
    private val onAddEmojiClickAction: (messageId: Int) -> Unit
) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_time_divider -> TimeDividerViewHolder(view)
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