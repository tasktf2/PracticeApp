package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI

data class PeopleState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val users: List<UserItemUI>? = null,
    val visibleUsers: List<UserItemUI>? = null
) : BaseState