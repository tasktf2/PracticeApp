package com.setjy.practiceapp.presentation.ui.channels

import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*

class ChannelsReducer : Reducer<ChannelsAction, ChannelsState, BaseEffect> {
    override fun reduceToState(action: ChannelsAction, state: ChannelsState): ChannelsState {
        return when (action) {
            is ChannelsAction.ShowError -> state.copy(error = action.error)

            is ChannelsAction.ShowLoading -> state.copy(isLoading = true)

            is ChannelsAction.ShowStreams -> state.copy(
                isLoading = false, streams = action.streams,
                visibleItems = action.streams
            )

            is ChannelsAction.ShowToggleStream -> state.copy(
                streams = action.streams,
                visibleItems = action.items
            )

            is ChannelsAction.ShowSearchResult -> state.copy(visibleItems = action.streams)

            else -> state
        }
    }

    override fun reduceToEffect(
        action: ChannelsAction,
        state: ChannelsState
    ): Optional<BaseEffect> {
        return Optional.empty()
    }
}