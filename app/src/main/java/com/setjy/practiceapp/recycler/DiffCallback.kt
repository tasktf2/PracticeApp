package com.setjy.practiceapp.recycler

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
                oldItem.user_id == newItem.user_id
            }
            else -> oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {

        return when {
            oldItem is IncomingMessageUI && newItem is IncomingMessageUI -> {
                oldItem.avatar == newItem.avatar &&
                        oldItem.username == newItem.username &&
                        oldItem.message == newItem.message &&
                        oldItem.reactions == newItem.reactions

            }
            oldItem is OutgoingMessageUI && newItem is OutgoingMessageUI -> {
                oldItem.user_id == newItem.user_id &&
                oldItem.text == newItem.text
            }
            else -> oldItem.viewType == newItem.viewType
        }

    }
}