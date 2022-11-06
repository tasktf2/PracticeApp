package com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.recycler.base.BaseViewHolder
import com.setjy.practiceapp.presentation.base.recycler.base.HolderFactory
import com.setjy.practiceapp.presentation.ui.topic.holder.EmojiViewHolder

class BottomSheetHolderFactory(private val onEmojiClickAction: (emojiCode: String) -> Unit) :
    HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji -> EmojiViewHolder(view).apply {
                view.setOnClickListener { currentEmojiName?.let(onEmojiClickAction) }
            }
            else -> null
        }
    }
}