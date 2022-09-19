package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class TopicItemUI(
    val topicId: Int,
    val topicName: String,
    val messageCount: Int = 0,
    val parent:String,
    val isFound: Boolean = false,
    override val viewType: Int = R.layout.item_topic
) : ViewTyped