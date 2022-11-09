package com.setjy.practiceapp.presentation.ui.profile

import com.setjy.practiceapp.presentation.base.mvi.BaseAction

sealed class ProfileAction : BaseAction {

    object ShowLoading : ProfileAction()

    object LoadOwnUser : ProfileAction()

    data class ShowOwnUser(val user: UserItemUI) : ProfileAction()

    data class ShowError(val error: Throwable) : ProfileAction()
}
