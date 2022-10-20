package com.setjy.practiceapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.recycler.items.UserStatus

data class UsersResponse(@SerializedName("members") val members: List<UsersRemote>)

data class UsersRemote(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("delivery_email") val userEmail: String,
    @SerializedName("timezone") val userTimeZone: String
)

data class UserResponse(
    @SerializedName("id") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String,
)

data class UserStatusPresence(
    @SerializedName("presence") val presence: UserStatusResponse
)

data class UserStatusResponse(
    @SerializedName("aggregated") val statusAndTimestamp: UserStatusRemote
)

data class UserStatusRemote(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("status") val status: UserStatus
)