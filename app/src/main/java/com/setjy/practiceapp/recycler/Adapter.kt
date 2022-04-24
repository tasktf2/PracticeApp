package com.setjy.practiceapp.recycler

import androidx.recyclerview.widget.AsyncListDiffer
import com.setjy.practiceapp.recycler.base.BaseAdapter
import com.setjy.practiceapp.recycler.base.HolderFactory
import com.setjy.practiceapp.recycler.base.ViewTyped

class Adapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val differ = AsyncListDiffer(this, DiffCallback())

    override var items: List<T>
        get() = differ.currentList as List<T>
        set(newItems) {
            differ.submitList(newItems)
        }
}