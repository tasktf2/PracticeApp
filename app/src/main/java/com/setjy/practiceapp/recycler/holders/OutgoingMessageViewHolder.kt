package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.ItemMsgOutgoingBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped

data class OutgoingMessageUI(val text: String, override val viewType: Int = R.layout.item_msg_outgoing) :
    ViewTyped

class OutgoingMessageViewHolder(val view: View) : BaseViewHolder<OutgoingMessageUI>(view) {

    private val binding: ItemMsgOutgoingBinding = ItemMsgOutgoingBinding.bind(view)

    override fun bind(item: OutgoingMessageUI) {
        binding.tvMessage.text = item.text
    }
}