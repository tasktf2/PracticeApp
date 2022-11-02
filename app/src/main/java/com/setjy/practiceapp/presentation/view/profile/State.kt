package com.setjy.practiceapp.presentation.view.profile

import com.setjy.practiceapp.presentation.model.UserItemUI

data class State(
    val userItemUI: UserItemUI? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false
)