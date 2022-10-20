package com.setjy.practiceapp.data.network.response

import com.google.gson.annotations.SerializedName

data class StreamsResponse(
    @SerializedName(value = "streams", alternate = ["subscriptions"])
    val streams: List<StreamsRemote>
)

data class StreamsRemote(
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("name") val streamName: String,
    @SerializedName("color") val streamColor: String?
)

data class Narrow(
    @SerializedName("operator") val operator: String,
    @SerializedName("operand") val operand: String,
)