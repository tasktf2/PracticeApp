package com.setjy.practiceapp.presentation.ui.topic.holder

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemEmojiBinding
import com.setjy.practiceapp.presentation.base.recycler.base.BaseViewHolder
import com.setjy.practiceapp.presentation.model.EmojiUI

class EmojiViewHolder(view: View) : BaseViewHolder<EmojiUI>(view) {

    var currentEmojiName: String? = null

    private val binding: ItemEmojiBinding by viewBinding()

    override fun bind(item: EmojiUI) {
        binding.tvEmoji.text = item.codeString
        currentEmojiName = item.emojiName
    }
}