package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
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
                val mutableItems: MutableList<ViewTyped> = mutableListOf()
                val streams = lastState.streams.orEmpty().map { stream ->
                    when (stream.streamId) {
                        action.stream.streamId -> stream.copy(isExpanded = !stream.isExpanded)
                        else -> stream
                    }
                }.map { stream ->
                    when {
                        stream.isExpanded -> {
                            mutableItems.add(stream)
                            mutableItems.addAll(stream.listOfTopics)
                            stream
                        }
                        else -> {
                            mutableItems.add(stream)
                            stream
                        }
                    }
                }
                ChannelsAction.ShowToggleStream(streams = streams, items = mutableItems)
            }
    }
}