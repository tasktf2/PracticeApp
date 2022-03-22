package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemEmojiBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.reactions.EmojiNCS

class EmojiViewHolder(view: View, click: (View) -> Unit) : BaseViewHolder<EmojiNCS>(view) {

    init {
        view.setOnClickListener(click)
    }

    private val binding: ItemEmojiBinding by viewBinding()

    override fun bind(item: EmojiNCS) {
        binding.tvEmoji.text = item.getCodeString()
    }
}