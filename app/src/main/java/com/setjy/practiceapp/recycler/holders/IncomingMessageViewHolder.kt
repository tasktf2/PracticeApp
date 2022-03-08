package com.setjy.practiceapp.recycler.holders

import android.util.Log
import android.view.View
import com.setjy.practiceapp.EmojiData
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.view.CustomViewGroup
import com.setjy.practiceapp.view.EmojiView

data class IncomingMessageUI(
    val avatar: Int,
    val username: String,
    val message: String,
    val reactionsList: List<EmojiData>? = null,
    override val viewType: Int = R.layout.item_msg_incoming,
) : ViewTyped

class IncomingMessageViewHolder(
    val view: View, longClick: (View) -> Unit
) : BaseViewHolder<IncomingMessageUI>(view) {

    private val messageView: CustomViewGroup = view as CustomViewGroup

    init {
        view.setOnClickListener(longClick)
    }

    override fun bind(item: IncomingMessageUI) {
        with(messageView.binding) {
            ivAvatar.setImageResource(item.avatar)
            tvUsername.text = item.username
            tvMessage.text = item.message
            Log.d("xxx", "$item")
            if (!item.reactionsList.isNullOrEmpty()) {
                item.reactionsList.forEach { listItem ->
                    flexbox.addView(EmojiView(view.context).apply {
                        this.emojiUnicode = listItem.emoji
                        this.emojiCounter = listItem.counter
                    })
                }
            }
        }
    }
}