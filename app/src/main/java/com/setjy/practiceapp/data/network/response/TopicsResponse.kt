package com.setjy.practiceapp.data.network.response

import com.google.gson.annotations.SerializedName

data class TopicsResponse(val topics: List<TopicsRemote>)

data class TopicsRemote(
    @SerializedName("max_id") val topicId: Int,
    @SerializedName("name") val topicName: String
)