package com.setjy.practiceapp.recycler

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.holders.IncomingMessageViewHolder
import com.setjy.practiceapp.recycler.holders.OutgoingMessageViewHolder

class ChatHolderFactory(private val longClick: (View) -> Unit) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
//            R.layout.item_msg_incoming -> IncomingMessageViewHolder(view, longClick)
            R.layout.custom_view_group -> IncomingMessageViewHolder(view, longClick)
            R.layout.item_msg_outgoing -> OutgoingMessageViewHolder(view)
            else -> null
        }
    }
}