package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchMessagesMiddleware @Inject constructor() :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.SearchChanged::class.java)
            .debounce(DEBOUNCE_SEARCH_MS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .withLatestFrom(state) { action, state ->

                val query = action.search.trim()

                val foundIndices = if (query.isBlank()) emptyList()
                else {
                    state.messages.orEmpty()
                        .mapIndexedNotNull { index, item ->
                            val isMatch = item.message.contains(query, ignoreCase = true)

                            if (isMatch) index else null
                        }
                }
                TopicAction.FoundIndices(foundIndices)
            }
    }

    private companion object {
        const val DEBOUNCE_SEARCH_MS = 300L
    }

}
