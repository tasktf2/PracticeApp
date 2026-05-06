package com.setjy.practiceapp.presentation.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.ui.profile.UserStatus.ACTIVE
import com.setjy.practiceapp.presentation.ui.profile.UserStatus.IDLE
import com.setjy.practiceapp.presentation.ui.profile.UserStatus.OFFLINE
import com.setjy.practiceapp.presentation.ui.theme.AppTheme

data class ProfileState(
    val userItemUI: UserItemUI? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : BaseState

data class UserItemUI(
    val userId: Int,
    val avatarUrl: String,
    val fullName: String,
    val userEmail: String,
    val status: UserStatus,
    override val uid: Int = userId,
    override val viewType: Int = R.layout.item_user
) : ViewTyped, Item

enum class UserStatus(val color: Int) {
    @SerializedName("active")
    ACTIVE(R.color.online_green),

    @SerializedName("idle")
    IDLE(R.color.idle_orange),

    @SerializedName("offline")
    OFFLINE(R.color.inactive_grey)
}

val UserStatus.colorX: Color
    @Composable
    get() = when (this) {
        ACTIVE -> AppTheme.colors.online
        IDLE -> AppTheme.colors.idle
        OFFLINE -> AppTheme.colors.disabled
    }

