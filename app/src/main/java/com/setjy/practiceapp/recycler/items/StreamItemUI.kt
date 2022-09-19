package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class StreamItemUI(
    val streamId:Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val listOfTopics: List<TopicItemUI> = listOf(),
    val isFound: Boolean = false,
    var isExpanded: Boolean = false,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped