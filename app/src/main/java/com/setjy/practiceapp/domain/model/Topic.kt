package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.model.TopicItemUI

data class Topic(
    val topicId: Int,
    val topicName: String,
    val streamId: Int
) : Model() {
    fun toPresentation(streamName: String, backgroundColor: String?) = TopicItemUI(
        topicId = topicId,
        topicName = topicName,
        parentId = streamId,
        parentName = streamName,
        backgroundColor = backgroundColor
    )

}

//class TopicMapper : DomainMapper<TopicItemUI, TopicDomain> { todo delete?
//    override fun mapToPresentation(model: TopicDomain): TopicItemUI =
//        TopicItemUI(
//            topicId = model.topicId,
//            topicName = model.topicName,
//            parentId = model.streamId,
//            parentName = model.streamName,
//            backgroundColor = null
//        )
//}