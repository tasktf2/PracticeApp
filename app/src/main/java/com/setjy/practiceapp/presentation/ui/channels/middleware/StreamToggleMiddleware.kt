package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import io.reactivex.rxjava3.core.Observable

class StreamToggleMiddleware : Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.ToggleStream::class.java)
            .withLatestFrom(state) { action, lastState ->
                ChannelsAction.ShowToggleStream(
                    streams = lastState.streams.orEmpty()
                        .map { stream ->
                            if (stream.streamId == action.stream.streamId) {
                                stream.copy(isExpanded = !stream.isExpanded)
                            } else {
                                stream
                            }
                        },
                    items = lastState.streams.orEmpty()
                        .map { stream ->
                            if (stream.streamId == action.stream.streamId) {
                                stream.copy(isExpanded = !stream.isExpanded)
                            } else {
                                stream
                            }
                        }
                        .flatMap { stream ->
                            if (stream.isExpanded) {
                                listOf(stream) + stream.listOfTopics
                            } else {
                                listOf(stream)
                            }
                        })
            }
    }
}