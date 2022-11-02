package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.base.ModelEntity
import com.setjy.practiceapp.domain.model.UserDomain
import com.setjy.practiceapp.presentation.model.UserStatus

data class UsersResponse(@SerializedName("members") val members: List<UsersRemote>) : ModelEntity()

data class UsersRemote(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("delivery_email") val userEmail: String,
    @SerializedName("timezone") val userTimeZone: String
) {
    fun toDomain(userStatus: UserStatus) = UserDomain(
        userId = userId,
        fullName = fullName,
        avatarUrl = avatarUrl,
        userEmail = userEmail,
        userStatus = userStatus
    )
}

data class UserResponse(
    @SerializedName("id") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("full_name") val fullName: String
)

data class UserStatusPresence(
    @SerializedName("presence") val presence: UserStatusResponse
) : ModelEntity()

data class UserStatusResponse(
    @SerializedName("aggregated") val statusAndTimestamp: UserStatusRemote
)

data class UserStatusRemote(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("status") val status: UserStatus
)