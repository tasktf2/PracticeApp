package com.setjy.practiceapp.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI

class DiffCallback : DiffUtil.ItemCallback<ViewTyped>() {

    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return when {
            oldItem is IncomingMessageUI && newItem is IncomingMessageUI -> {
                oldItem.messageId == newItem.messageId
            }
            oldItem is OutgoingMessageUI && newItem is OutgoingMessageUI -> {
                oldItem.messageId == newItem.messageId
            }
            else -> oldItem == newItem
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem == newItem
    }
}