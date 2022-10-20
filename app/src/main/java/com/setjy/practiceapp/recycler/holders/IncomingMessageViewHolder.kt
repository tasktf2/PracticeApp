package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.bumptech.glide.Glide
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.MessageUI
import com.setjy.practiceapp.view.MessageViewGroup

class IncomingMessageViewHolder(val view: View) : BaseViewHolder<MessageUI>(view) {

    private val messageView: MessageViewGroup = view as MessageViewGroup

    var currentMessageId: Int? = null

    var onEmojiClick: ((messageId: Int, emojiName: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: ((messageId: Int) -> Unit)? = null

    override fun bind(item: MessageUI) {
        currentMessageId = item.messageId
        with(messageView.binding) {
            if (!item.isOutgoingMessage) {
                Glide.with(view)
                    .load(item.avatarUrl)
                    .centerCrop()
                    .into(ivAvatar)
            }
            tvUsername.text = item.username
            tvMessage.text = item.message
            tvTimestamp.text = item.timestamp
        }
        if (item.reactions.isNotEmpty()) {
            messageView.setEmojis(item.reactions)

            messageView.setOnEmojiClickListener { emojiName, emojiCode ->
                onEmojiClick?.invoke(item.messageId, emojiName, emojiCode)
            }
            messageView.setOnAddEmojiClickListener {
                onAddEmojiClick?.invoke(item.messageId)
            }
        } else {
            messageView.binding.flexbox.removeAllViews()
        }
    }
}