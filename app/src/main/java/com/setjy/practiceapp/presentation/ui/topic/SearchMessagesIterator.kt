package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.model.MessageUI

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

    private fun nextIndex(): Int = ++index

    private fun previousIndex(): Int = --index

    private fun currentIndex(): Int = index

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

    fun currentMessage(): ViewTyped = (isFoundItems[currentIndex()])

    fun previousMessage(): ViewTyped = (isFoundItems[previousIndex()])

    fun nextMessage(): ViewTyped = (isFoundItems[nextIndex()])
}