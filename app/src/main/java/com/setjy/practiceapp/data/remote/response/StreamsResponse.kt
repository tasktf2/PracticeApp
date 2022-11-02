package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.data.base.ModelRemote

data class StreamsResponse(
    @SerializedName(value = "streams", alternate = ["subscriptions"])
    val streams: List<StreamRemote>
)

data class StreamRemote(
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("name") val streamName: String,
    @SerializedName("color") val streamColor: String?
)

data class Narrow(
    @SerializedName("operator") val operator: String,
    @SerializedName("operand") val operand: String,
)