package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchMessagesMiddleware @Inject constructor() :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions
            .observeOn(Schedulers.computation())
            .filter { action ->
                action is TopicAction.SearchChanged ||
                        action is TopicAction.ShowMessages ||
                        action is TopicAction.ShowPaginationResult ||
                        action is TopicAction.ShowEvents
            }
            .debounce { action ->
                if (action is TopicAction.SearchChanged) {
                    Observable.timer(DEBOUNCE_SEARCH_MS, TimeUnit.MILLISECONDS)
                } else {
                    Observable.just(0L)
                }
            }
            .withLatestFrom(state) { action, state ->

                val query =
                    (if (action is TopicAction.SearchChanged) action.search else state.search)
                        .trim()

                val messagesForSearch = when (action) {
                    is TopicAction.ShowMessages -> action.messages
                    is TopicAction.ShowEvents -> action.messages
                    else -> state.messages.orEmpty()
                }
                val indices = if (query.isBlank()) emptyList()
                else {

                    messagesForSearch.mapIndexedNotNull { index, item ->
                        if (item.message.contains(query, ignoreCase = true)) index else null
                    }
                }
                TopicAction.FoundIndices(indices)
            }.distinctUntilChanged().ofType(TopicAction::class.java)
    }

    private companion object {
        const val DEBOUNCE_SEARCH_MS = 300L
    }
}
