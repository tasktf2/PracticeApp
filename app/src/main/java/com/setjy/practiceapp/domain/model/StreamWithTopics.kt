package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import javax.inject.Inject

data class StreamWithTopics(
    val streamId: Int,
    val streamName: String,
    val isSubscribed: Boolean = false,
    val backgroundColor: String?,
    val topics: List<Topic>
) : Model

class StreamMapper @Inject constructor() : DomainMapper<StreamWithTopics, StreamItemUI> {
    override fun mapToPresentation(model: StreamWithTopics): StreamItemUI = StreamItemUI(
        streamId = model.streamId,
        streamName = model.streamName,
        isSubscribed = model.isSubscribed,
        listOfTopics = model.topics.map {
            it.toPresentation(
                streamName = model.streamName,
                backgroundColor = model.backgroundColor
            )
        }
    )
}