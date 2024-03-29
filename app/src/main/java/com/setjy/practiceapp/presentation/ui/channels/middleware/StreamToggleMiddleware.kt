package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class StreamToggleMiddleware @Inject constructor() : Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.ToggleStream::class.java)
            .withLatestFrom(state) { action, lastState ->
                val streams = lastState.streams.orEmpty()
                    .map { stream ->
                        if (stream.streamId == action.stream.streamId) {
                            stream.copy(isExpanded = !stream.isExpanded)
                        } else {
                            stream
                        }
                    }
                ChannelsAction.ShowToggleStream(
                    streams = streams,
                    items = streams
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