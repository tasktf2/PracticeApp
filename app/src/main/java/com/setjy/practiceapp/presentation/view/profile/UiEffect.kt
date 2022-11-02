package com.setjy.practiceapp.presentation.view.profile

sealed class UiEffect {

    object StartLoadOwnUser : UiEffect()

    object OwnUserLoaded : UiEffect()

    data class ErrorLoadOwnUser(val error: Throwable) : UiEffect()
}
