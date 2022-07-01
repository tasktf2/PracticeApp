package com.setjy.practiceapp.chat

import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.IncomingMessageUI
import com.setjy.practiceapp.recycler.items.OutgoingMessageUI

class SearchMessagesIterator : ListIterator<ViewTyped> {

    private var index: Int = 0

    private var items: List<ViewTyped> = emptyList()

    override fun hasNext(): Boolean {
        return items.subList(index, items.size).find { item ->
            when (item) {
                is IncomingMessageUI -> item.isFound
                is OutgoingMessageUI -> item.isFound
                else -> false
            }
        } != null
    }

    override fun next(): ViewTyped {
        return items[index++]
    }

    override fun nextIndex(): Int {
        return index++
    }

    override fun hasPrevious(): Boolean {
        return items.subList(0, index).find { item ->
            when (item) {
                is IncomingMessageUI -> item.isFound
                is OutgoingMessageUI -> item.isFound
                else -> false
            }
        } != null
    }

    override fun previous(): ViewTyped {
        return items[index--]
    }

    override fun previousIndex(): Int {
        return index--
    }

    fun setItems(newItems: List<ViewTyped>) {
        items = newItems
    }

    fun resetIndex() {
        index = 0
    }

}