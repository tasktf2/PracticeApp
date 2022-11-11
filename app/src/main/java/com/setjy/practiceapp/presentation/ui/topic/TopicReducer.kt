package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*

class TopicReducer : Reducer<TopicAction, TopicState, TopicEffect> {
    override fun reduceToState(action: TopicAction, state: TopicState): TopicState {
        return when (action) {
            TopicAction.ShowLoading -> state.copy(isLoading = true)

            is TopicAction.ShowMessages -> state.copy(isLoading = false, messages = action.messages)

            is TopicAction.QueueRegistered -> state.copy(isRegisteredQueue = true)

            is TopicAction.ShowError -> state.copy(error = action.error)

            is TopicAction.ShowMessagesOnScroll -> state.copy(
                messages = state.messages.orEmpty() + action.messagesFromScroll,
                onScrollIsLoading = action.onScrollIsLoading,
                onScrollIsLastPage = action.onScrollIsLastPage
            )

            else -> state
        }
    }

    override fun reduceToEffect(action: TopicAction, state: TopicState): Optional<TopicEffect> {
        return Optional.empty()
    }
}