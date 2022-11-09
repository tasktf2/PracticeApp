package com.setjy.practiceapp.presentation.ui.profile

import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*

class ProfileReducer : Reducer<ProfileAction, ProfileState, BaseEffect> {
    override fun reduceToState(action: ProfileAction, state: ProfileState): ProfileState {
        return when (action) {
            is ProfileAction.LoadOwnUser -> state.copy(
                userItemUI = null,
                error = null,
                isLoading = false
            )

            is ProfileAction.ShowLoading -> state.copy(
                userItemUI = null,
                error = null,
                isLoading = true
            )

            is ProfileAction.ShowOwnUser -> state.copy(
                userItemUI = action.user,
                error = null,
                isLoading = false
            )

            is ProfileAction.ShowError -> state.copy(
                userItemUI = null,
                error = action.error,
                isLoading = false
            )
        }
    }

    override fun reduceToEffect(action: ProfileAction, state: ProfileState): Optional<BaseEffect> {
        return Optional.empty()
    }
}