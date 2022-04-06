package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.databinding.ItemMsgOutgoingBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI


class OutgoingMessageViewHolder(val view: View) : BaseViewHolder<OutgoingMessageUI>(view) {

    private val binding: ItemMsgOutgoingBinding = ItemMsgOutgoingBinding.bind(view)

    var currentMessageId: String? = null

    var onEmojiClick: ((messageId: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: ((messageId: String) -> Unit)? = null

    override fun bind(item: OutgoingMessageUI) {
        currentMessageId = item.messageId
        binding.tvMessage.text = item.text

        if (!item.reactions.isNullOrEmpty()) {
            binding.flexbox.setEmojis(item.reactions)
            binding.flexbox.onEmojiClick =
                { emojiCode -> onEmojiClick?.invoke(item.messageId, emojiCode) }
            binding.flexbox.onAddEmojiClick = { onAddEmojiClick?.invoke(item.messageId) }
        } else {
            binding.flexbox.removeAllViews()
        }
    }


}
