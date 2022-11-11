package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.model.MessageUI

data class TopicState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val messages: List<MessageUI>? = null,
    val onScrollIsLoading: Boolean = false,
    val onScrollIsLastPage: Boolean = false,
    val isRegisteredQueue: Boolean = false
) : BaseState
