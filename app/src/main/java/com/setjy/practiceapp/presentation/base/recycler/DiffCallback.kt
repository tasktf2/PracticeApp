package com.setjy.practiceapp.presentation.base.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

class DiffCallback : DiffUtil.ItemCallback<ViewTyped>() {

    override fun areItemsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem.uid == newItem.uid
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ViewTyped, newItem: ViewTyped): Boolean {
        return oldItem == newItem
    }
}