package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.databinding.ItemMsgOutgoingBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI


class OutgoingMessageViewHolder(val view: View) : BaseViewHolder<OutgoingMessageUI>(view) {

    private val binding: ItemMsgOutgoingBinding = ItemMsgOutgoingBinding.bind(view)

    var currentMessageId: Int? = null

    var onEmojiClick: ((messageId: Int, emojiName: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: ((messageId: Int) -> Unit)? = null

    override fun bind(item: OutgoingMessageUI) {
        currentMessageId = item.messageId
        with(binding) {
            tvMessage.text = item.message
            tvTimestamp.text = item.timestamp

            if (!item.reactions.isNullOrEmpty()) {
                flexbox.setEmojis(item.reactions)
                flexbox.onEmojiClick =
                    {emojiName, emojiCode -> onEmojiClick?.invoke(item.messageId,emojiName, emojiCode) }
                flexbox.onAddEmojiClick = { onAddEmojiClick?.invoke(item.messageId) }
            } else {
                flexbox.removeAllViews()
            }
        }
    }


}
