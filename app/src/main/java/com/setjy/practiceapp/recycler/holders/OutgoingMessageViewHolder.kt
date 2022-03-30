package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.databinding.ItemMsgOutgoingBinding
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI


class OutgoingMessageViewHolder(val view: View) : BaseViewHolder<OutgoingMessageUI>(view) {

    private val binding: ItemMsgOutgoingBinding = ItemMsgOutgoingBinding.bind(view)

    override fun bind(item: OutgoingMessageUI) {
        binding.tvMessage.text = item.text
    }


}
