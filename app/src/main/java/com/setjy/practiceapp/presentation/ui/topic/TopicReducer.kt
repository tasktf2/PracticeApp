package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*
import javax.inject.Inject

class TopicReducer @Inject constructor() : Reducer<TopicAction, TopicState, TopicEffect> {
    override fun reduceToState(action: TopicAction, state: TopicState): TopicState {
        return when (action) {
            TopicAction.ShowLoading -> state.copy(isLoading = true)

            is TopicAction.ShowMessages -> state.copy(isLoading = false, messages = action.messages)

            is TopicAction.ShowError -> state.copy(error = action.error)

            is TopicAction.ShowPaginationResult -> state.copy(
                messages = state.messages.orEmpty() + action.messagesFromScroll,
                isPaginationLoading = false,
                isPaginationLastPage = action.isLastPage
            )
            is TopicAction.ShowEvents -> state.copy(messages = action.messages)

            is TopicAction.StartPagination -> state.copy(isPaginationLoading = true)
            else -> state
        }
    }

    override fun reduceToEffect(action: TopicAction, state: TopicState): Optional<TopicEffect> {
        return when (action) {
            is TopicAction.QueueRegistered -> Optional.of(
                TopicEffect.GetEvents(queueId = action.queueId, lastEventId = action.lastEventId)
            )
            is TopicAction.ShowEvents -> Optional.of(
                TopicEffect.GetEvents(queueId = action.queueId, lastEventId = action.lastEventId)
            )
            is TopicAction.ShowBottomSheetFragment -> Optional.of(
                TopicEffect.ShowBottomSheetFragment(
                    action.messageId
                )
            )

            else -> Optional.empty()
        }
    }
}