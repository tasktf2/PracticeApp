package com.setjy.practiceapp.recycler.holders

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemMsgOutgoingBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped

class OutgoingMessageUI(val text: String, override val viewType: Int = R.layout.item_msg_outgoing) :
    ViewTyped

class OutgoingMessageViewHolder(val view: View) : BaseViewHolder<OutgoingMessageUI>(view) {

    private val binding: ItemMsgOutgoingBinding by viewBinding()
    override fun bind(item: OutgoingMessageUI) {
        binding.tvMessage.text = item.text
    }
}