package com.setjy.practiceapp.recycler.items

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class UserItemUI(
    val userId: Int,
    val avatarUrl: String,
    val fullName: String,
    val userEmail: String,
    val status: UserStatus,
    val timezone: String,
    override val uid: Int = userId,
    override val viewType: Int = R.layout.item_user
) : ViewTyped

enum class UserStatus(val color: Int) {
    @SerializedName("active")
    ACTIVE(R.color.online_green),

    @SerializedName("idle")
    IDLE(R.color.idle_orange),

    @SerializedName("offline")
    OFFLINE(R.color.inactive_grey)
}
