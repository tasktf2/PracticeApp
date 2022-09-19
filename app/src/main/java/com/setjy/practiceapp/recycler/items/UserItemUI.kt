package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.profile.UserStatus
import com.setjy.practiceapp.recycler.base.ViewTyped

data class UserItemUI(
    val userId:Int,
    val avatarUrl: String,
    val fullName: String,
    val userEmail: String,
    val status: UserStatus,
    override val viewType: Int = R.layout.item_user
) : ViewTyped