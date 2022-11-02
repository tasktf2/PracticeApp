package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.data.base.ModelRemote
import com.setjy.practiceapp.domain.model.Topic

data class TopicsResponse(@SerializedName("topics") val topics: List<TopicsRemote>) : ModelEntity()

data class TopicsRemote(
    @SerializedName("max_id") val topicId: Int,
    @SerializedName("name") val topicName: String
)  {
    fun toDomain(streamId: Int) =
        Topic(topicId = topicId, topicName = topicName, streamId = streamId)
}