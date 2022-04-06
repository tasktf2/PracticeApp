package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemEmojiBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.EmojiUI

class EmojiViewHolder(view: View) : BaseViewHolder<EmojiUI>(view) {

    var currentEmojiCode: String? = null

    private val binding: ItemEmojiBinding by viewBinding()

    override fun bind(item: EmojiUI) {
        binding.tvEmoji.text = item.codeString
        currentEmojiCode = item.code
    }
}