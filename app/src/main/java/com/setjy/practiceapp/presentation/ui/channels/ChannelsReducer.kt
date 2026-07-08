package com.setjy.practiceapp.presentation.ui.channels

import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.Optional
import javax.inject.Inject

class ChannelsReducer @Inject constructor() : Reducer<ChannelsAction, ChannelsState, BaseEffect> {
    override fun reduceToState(action: ChannelsAction, state: ChannelsState): ChannelsState {
        return when (action) {
            is ChannelsAction.ShowError -> state.copy(error = action.error)
            is ChannelsAction.ShowLoading -> state.copy(isLoading = true)
            is ChannelsAction.ShowStreams -> {
                if (action.isSubscribed) {
                    state.copy(
                        isLoading = false, streamsSubscribed = action.streams,
                        visibleItemsSubscribed = action.streams
                    )
                } else {
                    state.copy(
                        isLoading = false, streams = action.streams,
                        visibleItems = action.streams
                    )
                }
            }

            is ChannelsAction.ShowCachedData -> {
                if (action.isSubscribed) {
                    state.copy(
                        isLoading = false, visibleItemsSubscribed = action.data,
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        visibleItems = action.data
                    )
                }
            }

            is ChannelsAction.ShowToggleStream -> if (action.isSubscribed) state.copy(
                streamsSubscribed = action.streams,
                visibleItemsSubscribed = action.items
            ) else state.copy(
                streams = action.streams,
                visibleItems = action.items
            )

            is ChannelsAction.SearchStreams -> state.copy(search = action.query)
            is ChannelsAction.ShowSearchResult -> state.copy(
                visibleItems = action.items,
                visibleItemsSubscribed = action.subItems,
                search = action.query
            )

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