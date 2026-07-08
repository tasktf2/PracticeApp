package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import com.setjy.practiceapp.presentation.ui.channels.TopicItemUI
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchStreamsMiddleware @Inject constructor() : Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.SearchStreams::class.java)
            .debounce(DEBOUNCE_SEARCH_MS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .withLatestFrom(state) { action, lastState ->

                ChannelsAction.ShowSearchResult(
                    items = search(action.query, lastState.streams),
                    subItems = search(action.query, lastState.streamsSubscribed),
                    query = action.query
                )
            }
    }

    private fun search(
        query: String,
        streams: List<StreamItemUI>?
    ): List<ViewTyped> = if (query.isNotEmpty()) {
        streams.orEmpty().flatMap { stream ->
            listOf(stream.copy(isExpanded = true)) + stream.listOfTopics
        }
            .filter { item ->
                when (item) {
                    is StreamItemUI -> item.listOfTopics.any { topic ->
                        topic.topicName.contains(
                            query,
                            ignoreCase = true
                        )
                    }

                    is TopicItemUI -> item.topicName.contains(
                        query,
                        ignoreCase = true
                    )

                    else -> false
                }
            }
    } else {
        streams.orEmpty().map { it.copy(isExpanded = false) }
    }

    private companion object {
        const val DEBOUNCE_SEARCH_MS = 700L

    }
}
