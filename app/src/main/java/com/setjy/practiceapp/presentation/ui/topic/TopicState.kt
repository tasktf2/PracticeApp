package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.model.MessageUI

data class TopicState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val messages: List<MessageUI>? = null,
    val isPaginationLoading: Boolean = false,
    val isPaginationLastPage: Boolean = false,
    val message: String? = null,
    val search: String = "",
    val foundIndices: List<Int> = emptyList(),
    val isSearchVisible: Boolean = false
) : BaseState