package com.setjy.practiceapp.recycler.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.EmojiData
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.CustomViewGroupBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.view.EmojiView
import com.setjy.practiceapp.view.FlexboxLayout

class IncomingMessageUI(
    val avatar: Int,
    val username: String,
    val message: String,
    val reactionsList: List<EmojiData>? = null,
    override val viewType: Int = R.layout.custom_view_group,
) : ViewTyped

class IncomingMessageViewHolder(
    val view: View, longClick: (View) -> Unit
) : BaseViewHolder<IncomingMessageUI>(view) {
        private val binding: CustomViewGroupBinding by viewBinding()
//    val tvMessage = view.findViewById<TextView>(R.id.tv_message)
//    val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
//    val tvUsername = view.findViewById<TextView>(R.id.tv_username)
//    val flexbox = view.findViewById<FlexboxLayout>(R.id.flexbox)

    init {
        binding.tvMessage.setOnClickListener(longClick)
    }

    override fun bind(item: IncomingMessageUI) {
        with(binding) {
        ivAvatar.setImageResource(item.avatar)
        tvUsername.text = item.username
        tvMessage.text = item.message
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