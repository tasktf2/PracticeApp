package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class StreamItemUI(
    val streamName: String,
    val isSubscribed: Boolean = false,
    val listOfTopics: List<ViewTyped> = listOf(),
    val isFound: Boolean = false,
    override val viewType: Int = R.layout.item_stream
) : ViewTyped