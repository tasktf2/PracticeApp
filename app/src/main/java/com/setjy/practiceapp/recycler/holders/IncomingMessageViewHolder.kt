package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.view.MessageViewGroup

class IncomingMessageViewHolder(val view: View) : BaseViewHolder<IncomingMessageUI>(view) {

    private val messageView: MessageViewGroup = view as MessageViewGroup

    var currentMessageId: String? = null

    var onEmojiClick: ((messageId: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: ((messageId: String) -> Unit)? = null

    override fun bind(item: IncomingMessageUI) {
        currentMessageId = item.messageId
        with(messageView.binding) {
            ivAvatar.setImageResource(item.avatar)
            tvUsername.text = item.username
            tvMessage.text = item.message
        }
        messageView.setEmojis(item.reactions)
        messageView.setOnEmojiClickListener { emojiCode ->
            onEmojiClick?.invoke(item.messageId, emojiCode)
        }
        messageView.setOnAddEmojiClickListener {
            onAddEmojiClick?.invoke(item.messageId)
        }
    }
}