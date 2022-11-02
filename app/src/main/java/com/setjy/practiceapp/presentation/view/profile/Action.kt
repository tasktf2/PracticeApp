package com.setjy.practiceapp.presentation.view.profile

import com.setjy.practiceapp.presentation.model.UserItemUI

sealed class Action {

    object LoadingOwnUser : Action()

    data class OwnUserLoaded(val user: UserItemUI) : Action()

    data class ErrorLoadOwnUser(val error: Throwable) : Action()
}
