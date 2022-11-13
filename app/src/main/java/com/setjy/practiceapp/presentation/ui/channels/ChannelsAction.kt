package com.setjy.practiceapp.presentation.ui.channels

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

sealed class ChannelsAction : BaseAction {

    data class LoadStreams(val isSubscribed: Boolean) : ChannelsAction()

    object ShowLoading : ChannelsAction()

    data class ShowStreams(val streams: List<StreamItemUI>) : ChannelsAction()

    data class ShowError(val error: Throwable) : ChannelsAction()

    data class ToggleStream(val stream: StreamItemUI) : ChannelsAction()

    data class ShowToggleStream(val streams: List<StreamItemUI>, val items: List<ViewTyped>) :
        ChannelsAction()

    data class SearchStreams(val query: String) : ChannelsAction()

    data class ShowSearchResult(val items: List<ViewTyped>) : ChannelsAction()
}