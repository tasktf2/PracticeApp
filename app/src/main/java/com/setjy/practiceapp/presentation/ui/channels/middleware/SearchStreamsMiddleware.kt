package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import com.setjy.practiceapp.presentation.ui.channels.TopicItemUI
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchStreamsMiddleware @Inject constructor() : Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.SearchStreams::class.java)
            .distinctUntilChanged()
            .withLatestFrom(state) { action, lastState ->
                ChannelsAction.ShowSearchResult(
                    if (action.query.isNotEmpty()) {
                        lastState.streams.orEmpty()
                            .flatMap { stream ->
                                listOf(stream.copy(isExpanded = true)) + stream.listOfTopics
                            }.filter { item ->
                                when (item) {
                                    is StreamItemUI -> item.listOfTopics.any { topic ->
                                        topic.topicName.contains(
                                            action.query,
                                            ignoreCase = true
                                        )
                                    }
                                    is TopicItemUI -> item.topicName.contains(
                                        action.query,
                                        ignoreCase = true
                                    )
                                    else -> false
                                }
                            }
                    } else {
                        lastState.streams.orEmpty().map { it.copy(isExpanded = false) }
                    }
                )
            }
    }
}