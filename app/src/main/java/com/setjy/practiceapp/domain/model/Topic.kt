package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.ui.channels.TopicItemUI

data class Topic(
    val topicId: Int,
    val topicName: String,
    val streamId: Int
) : Model {
    fun toPresentation(streamName: String, backgroundColor: String?) = TopicItemUI(
        topicId = topicId,
        topicName = topicName,
        parentId = streamId,
        parentName = streamName,
        backgroundColor = backgroundColor
    )
}