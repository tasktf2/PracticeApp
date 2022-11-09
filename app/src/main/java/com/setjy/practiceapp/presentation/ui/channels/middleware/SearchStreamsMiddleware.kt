package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import com.setjy.practiceapp.presentation.ui.channels.TopicItemUI
import io.reactivex.rxjava3.core.Observable

class SearchStreamsMiddleware : Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.SearchStreams::class.java)
            .distinct()
            .withLatestFrom(state) { action, lastState ->
                ChannelsAction.ShowSearchResult(
                    if (action.query.isNotEmpty()) {
                        val mutableItems = mutableListOf<ViewTyped>()
                        putExpandedStreamsWithTopics(lastState.streams.orEmpty(), mutableItems)
                        filterItems(mutableItems, action)
                    } else {
                        lastState.streams.orEmpty().map { it.copy(isExpanded = false) }
                    }
                )
            }
    }

    private fun putExpandedStreamsWithTopics(
        streams: List<StreamItemUI>,
        mutableItems: MutableList<ViewTyped>
    ) {
        streams.map {
            mutableItems.add(it.copy(isFound = false))
            mutableItems.addAll(it.listOfTopics.map { topic -> topic.copy(isFound = false) })
            it
        }
    }

    private fun filterItems(
        mutableItems: MutableList<ViewTyped>,
        action: ChannelsAction.SearchStreams
    ) = mutableItems.map { item ->
        setIsFound(item, action.query)
    }
        .filter {
            when (it) {
                is StreamItemUI -> it.isFound
                is TopicItemUI -> it.isFound
                else -> false
            }
        }

    private fun setIsFound(
        item: ViewTyped,
        query: String
    ) = when (item) {
        is StreamItemUI -> {
            item.copy(isExpanded = true, isFound = item.listOfTopics.any {
                it.topicName.contains(query, ignoreCase = true)
            })
        }
        is TopicItemUI -> item.copy(isFound = item.topicName.contains(query, ignoreCase = true))
        else -> item
    }
}