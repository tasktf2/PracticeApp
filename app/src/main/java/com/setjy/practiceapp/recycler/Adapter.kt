package com.setjy.practiceapp.recycler

import com.setjy.practiceapp.recycler.base.BaseAdapter
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped

class Adapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {
    private val localItems: MutableList<T> = mutableListOf()
    override var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}