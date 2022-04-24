package com.setjy.practiceapp.hw_4.channels

import android.view.View
import android.widget.ImageView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemStreamBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped

//todo в айтемы
class StreamItemUI(
    val streamName: String,
    val isSubscribed: Boolean = false,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped

class StreamItemViewHolder(val view: View) : BaseViewHolder<StreamItemUI>(view) {

    private val binding: ItemStreamBinding by viewBinding()

    var onStreamClick: ((streamName: String, arrow:ImageView?) -> Unit)? = null

    var arrow:ImageView?=null

    override fun bind(item: StreamItemUI) {
        arrow=binding.ivArrow
        binding.tvStream.text = item.streamName
        binding.streamContainer.setOnClickListener {
            onStreamClick?.invoke(item.streamName, arrow)
        }
    }
}