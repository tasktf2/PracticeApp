package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemStreamBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.StreamItemUI

class StreamItemViewHolder(val view: View) : BaseViewHolder<StreamItemUI>(view) {

    private val binding: ItemStreamBinding by viewBinding()

    var onStreamClick: ((stream: StreamItemUI) -> Unit)? = null

    override fun bind(item: StreamItemUI) {
        with(binding) {
            ivArrow.isSelected = item.isExpanded
            tvStream.text = item.streamName
            streamContainer.setOnClickListener {
                onStreamClick?.invoke(item)
                ivArrow.isSelected = item.isExpanded
            }
        }
    }
}