package com.setjy.practiceapp.recycler.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : ViewTyped>(internal val holderFactory: HolderFactory) :
    RecyclerView.Adapter<BaseViewHolder<ViewTyped>>() {

    abstract var items: List<T>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> =
        holderFactory(parent, viewType)

    override fun onBindViewHolder(holder: BaseViewHolder<ViewTyped>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewTyped>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) holder.bind(items[position], payloads)
        else onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    fun isEmpty(): Boolean = items.isEmpty()
}