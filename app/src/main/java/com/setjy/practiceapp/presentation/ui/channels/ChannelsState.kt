package com.setjy.practiceapp.presentation.ui.channels

import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

data class ChannelsState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val streams: List<StreamItemUI>? = null,
    val visibleItems: List<ViewTyped>? = null
) : BaseState

data class StreamItemUI(
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val listOfTopics: List<TopicItemUI> = listOf(),
    val isFound: Boolean = false,
    val isExpanded: Boolean = false,
    override val uid: Int = streamId,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped, Item

data class TopicItemUI(
    val topicId: Int,
    val topicName: String,
    val messageCount: Int = 0,
    val parentId: Int,
    val parentName: String,
    val backgroundColor: String?,
    val isFound: Boolean = false,
    override val uid: Int = topicId,
    override val viewType: Int = R.layout.item_topic
) : ViewTyped, Item