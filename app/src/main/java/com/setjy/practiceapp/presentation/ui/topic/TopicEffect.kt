package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.BaseEffect

sealed class TopicEffect : BaseEffect {
    data class GetEvents(val queueId: String, val lastEventId: Int) : TopicEffect()
    data class ShowBottomSheetFragment(val messageId: Int) : TopicEffect()
}