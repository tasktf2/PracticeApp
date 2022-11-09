package com.setjy.practiceapp.presentation.ui.channels

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

sealed class ChannelsAction : BaseAction {
    object LoadStreams : ChannelsAction()

    object ShowLoading : ChannelsAction()

    class ShowStreams(val streams: List<StreamItemUI>) : ChannelsAction()

    class ShowError(val error: Throwable) : ChannelsAction()

    class ToggleStream(val stream: StreamItemUI) : ChannelsAction()

    class ShowToggleStream(val streams: List<StreamItemUI>, val items: List<ViewTyped>) :
        ChannelsAction()

    class SearchStreams(val query: String) : ChannelsAction()

    class ShowSearchResult(val streams: List<ViewTyped>) : ChannelsAction()
}