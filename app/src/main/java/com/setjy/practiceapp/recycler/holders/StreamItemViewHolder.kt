package com.setjy.practiceapp.recycler.holders

import android.view.View
import android.widget.ImageView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.databinding.ItemStreamBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.StreamItemUI

class StreamItemViewHolder(val view: View) : BaseViewHolder<StreamItemUI>(view) {

    private val binding: ItemStreamBinding by viewBinding()

    var onStreamClick: ((streamName: String) -> Unit)? = null

    var arrow: ImageView? = null

    override fun bind(item: StreamItemUI) {
        arrow = binding.ivArrow.apply {
            isSelected = item.isExpanded
        }
        binding.tvStream.text = item.streamName
        binding.streamContainer.setOnClickListener {
            onStreamClick?.invoke(item.streamName)
            arrow!!.isSelected = item.isExpanded
        }
    }
}