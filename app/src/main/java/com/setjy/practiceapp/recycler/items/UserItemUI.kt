package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class UserItemUI(
    val userId: Int,
    val avatarUrl: String,
    val fullName: String,
    val userEmail: String,
    val status: UserStatus,
    val timezone: String,
    override val viewType: Int = R.layout.item_user
) : ViewTyped

enum class UserStatus(val color: Int) {
    ACTIVE(R.color.online_green),
    IDLE(R.color.idle_orange),
    OFFLINE(R.color.inactive_grey)
}
