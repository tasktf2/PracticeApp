package com.setjy.practiceapp.presentation.ui.profile

import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*
import javax.inject.Inject

class ProfileReducer @Inject constructor() : @JvmSuppressWildcards Reducer<ProfileAction, ProfileState, ProfileEffect> {
    override fun reduceToState(action: ProfileAction, state: ProfileState): ProfileState {
        return when (action) {
            is ProfileAction.LoadOwnUser -> state

            is ProfileAction.ShowLoading -> state.copy(isLoading = true)

            is ProfileAction.ShowOwnUser -> state.copy(
                userItemUI = action.user,
                isLoading = false,
                error = null
            )

            is ProfileAction.ShowError -> state.copy(error = action.error, isLoading = false)
        }
    }

    override fun reduceToEffect(
        action: ProfileAction,
        state: ProfileState
    ): Optional<ProfileEffect> {
        return Optional.empty()
    }
}