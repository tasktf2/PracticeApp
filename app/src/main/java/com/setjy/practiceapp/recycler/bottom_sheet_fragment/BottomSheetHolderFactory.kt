package com.setjy.practiceapp.recycler.bottom_sheet_fragment

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.holders.EmojiViewHolder

class BottomSheetHolderFactory(private val click: (View) -> Unit) : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_emoji -> EmojiViewHolder(view, click)
            else -> null
        }
    }
}