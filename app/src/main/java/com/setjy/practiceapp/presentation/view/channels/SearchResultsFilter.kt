package com.setjy.practiceapp.presentation.view.channels

import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.model.StreamItemUI
import com.setjy.practiceapp.presentation.model.TopicItemUI

class SearchResultsFilter {

    private var items: List<ViewTyped> = emptyList()

    var isFoundItems: List<ViewTyped> = emptyList()

    fun setItems(newItems: List<ViewTyped>) {
        items = newItems.map { item ->
            when (item) {
                is StreamItemUI -> item.copy(isFound = false)
                is TopicItemUI -> item.copy(isFound = false)
                else -> item
            }
        }
    }

    fun filterItems(query: String) {
        var mutableItems = mutableListOf<ViewTyped>()
        items.map { item ->
            if (item is StreamItemUI) {
                mutableItems.add(item)
                mutableItems.addAll(item.listOfTopics)
            }
        }
        mutableItems = mutableItems.map { item ->
            when (item) {
                is StreamItemUI -> {
                    item.copy(isExpanded = true,
                        isFound =
                        item.streamName.contains(query, ignoreCase = true)
                                || item.listOfTopics.any {
                            it.topicName.contains(query, ignoreCase = true)
                        }
                    )
                }
                is TopicItemUI -> if (item.topicName.contains(query, ignoreCase = true)) {
                    item.copy(isFound = true)
                } else {
                    item
                }
                else -> item
            }
        }.toMutableList()
        isFoundItems = mutableItems.filter {
            when (it) {
                is StreamItemUI -> it.isFound
                is TopicItemUI -> it.isFound
                else -> false
            }
        }
    }
}