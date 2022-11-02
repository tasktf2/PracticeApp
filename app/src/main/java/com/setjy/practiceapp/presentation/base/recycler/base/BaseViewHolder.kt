package com.setjy.practiceapp.presentation.base.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewTyped>(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(item: T) = Unit

    open fun bind(item: T, payloads: List<Any>) = Unit
}