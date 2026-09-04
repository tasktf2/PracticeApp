package com.setjy.practiceapp.presentation.ui.topic

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SearchMessagesIterator {

    private var index by mutableIntStateOf(START_INDEX)
    private var lastQuery = ""
    val currentMatch: Int
        get() = if (index == START_INDEX) 0 else index + 1

    var foundIndices: MutableState<List<Int>> = mutableStateOf(emptyList())
        private set

    val currentMatchIndex: Int?
        get() = foundIndices.value.getOrNull(index)

    fun hasNext(): Boolean = foundIndices.value.isNotEmpty() && index < foundIndices.value.lastIndex

    fun hasPrevious(): Boolean = index > 0

    private fun nextIndex(): Int = ++index

    private fun previousIndex(): Int = --index

    private fun currentIndex(): Int = index

    fun reset() {
        index = START_INDEX
        foundIndices.value = emptyList()
    }

    fun setMatches(items: List<Int>, search: String) {
        if (search.isBlank()) {
            return
        }
        val query = search.trim()
        if (lastQuery != query) {
            reset()
            lastQuery = query
            foundIndices.value = items
            nextIndex()
        } else {
            foundIndices.value = items
        }

    }

    fun currentMessage() = (foundIndices.value[currentIndex()])

    fun previousMessage() = (foundIndices.value[previousIndex()])

    fun nextMessage() = (foundIndices.value[nextIndex()])

    private companion object {
        const val START_INDEX = -1
    }
}