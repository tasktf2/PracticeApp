package com.setjy.practiceapp.presentation.model

import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

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