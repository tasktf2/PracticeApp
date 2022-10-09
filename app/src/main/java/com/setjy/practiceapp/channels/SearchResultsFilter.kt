package com.setjy.practiceapp.channels

import com.setjy.practiceapp.data.Data
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.items.StreamItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUI
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

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
                Data.getTopics(item).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { e->mutableItems.addAll(e) }
//                mutableItems.addAll(item.listOfTopics)
//                todo fix this
            }
        }
        mutableItems = mutableItems.map { item ->
            when (item) {
                is StreamItemUI -> {
                    item.copy(isExpanded = true,
                        isFound =
                        item.streamName.contains(query, ignoreCase = true)
                        //todo fix this вытащить из бд топики и прочекать их по айди или переделать стримы на бд и уи
//                                || item.listOfTopics.any {
//                            it.topicName.contains(query, ignoreCase = true)
//                        }
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