package com.setjy.practiceapp.presentation.ui.people

import androidx.compose.runtime.Immutable
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI

@Immutable
data class PeopleState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val users: List<UserItemUI>? = null,
    val visibleUsers: List<UserItemUI>? = null,
    val search: String = ""
) : BaseState