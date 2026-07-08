package com.setjy.practiceapp.presentation.ui.channels

import androidx.compose.runtime.Immutable
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

@Immutable
data class ChannelsState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val streams: List<StreamItemUI>? = null,
    val visibleItems: List<ViewTyped>? = null,
    val streamsSubscribed: List<StreamItemUI>? = null,
    val visibleItemsSubscribed: List<ViewTyped>? = null,
    val search: String = "",
    val selectedTab: Int = 0
) : BaseState

@Immutable
data class StreamItemUI(
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val listOfTopics: List<TopicItemUI> = listOf(),
    val isExpanded: Boolean = false,
    override val uid: Int = streamId,
) : ViewTyped, Item

@Immutable
data class TopicItemUI(
    val topicId: Int,
    val topicName: String,
    val messageCount: Int = 0,
    val parentId: Int,
    val parentName: String,
    val backgroundColor: String?,
    override val uid: Int = topicId,
) : ViewTyped, Item