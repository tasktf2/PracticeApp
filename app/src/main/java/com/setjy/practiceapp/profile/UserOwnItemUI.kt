package com.setjy.practiceapp.profile

import com.setjy.practiceapp.R

data class UserOwnItemUI(
    val userId: Int,
    val userFullName: String,
    val status: UserStatus,
    val avatarUrl: String,
    val timezone: String
)

enum class UserStatus(val color: Int) {
    ACTIVE(R.color.online_green),
    IDLE(R.color.idle_orange),
    OFFLINE(R.color.inactive_grey)
}
