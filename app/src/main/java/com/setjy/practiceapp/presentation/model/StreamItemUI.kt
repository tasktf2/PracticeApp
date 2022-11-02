package com.setjy.practiceapp.presentation.model

import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

data class StreamItemUI(
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val listOfTopics: List<TopicItemUI> = listOf(),
    val isFound: Boolean = false,
    var isExpanded: Boolean = false,
    override val uid: Int = streamId,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped, Item()