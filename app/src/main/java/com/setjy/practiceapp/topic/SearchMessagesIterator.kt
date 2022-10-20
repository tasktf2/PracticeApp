package com.setjy.practiceapp.topic

import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.MessageUI

class SearchMessagesIterator {

    private var index = 0

    private var items: List<ViewTyped> = emptyList()

    var isFoundItems: List<ViewTyped> = emptyList()

    fun hasNext(): Boolean {
        return index != isFoundItems.lastIndex
    }

    fun hasPrevious(): Boolean {
        return index != 0
    }

    fun nextIndex(): Int {
        return ++index
    }

    fun previousIndex(): Int {
        return --index
    }

    fun currentIndex(): Int = index

    fun resetIndex() {
        index = 0
    }

    fun setItems(newItems: List<ViewTyped>) {
        items = newItems
        if (newItems.isEmpty()) {
            isFoundItems = emptyList()
        }
    }

    fun totalFound() {
        isFoundItems = items.filter {
            when (it) {
                is MessageUI -> it.isFound
                else -> false
            }
        }
    }

    fun getMessageId(index: Int): Int = when (isFoundItems[index]) { //todo fix (messageId)
        is MessageUI -> (isFoundItems[index] as MessageUI).messageId
        else -> 0 // think about exception
    }
}