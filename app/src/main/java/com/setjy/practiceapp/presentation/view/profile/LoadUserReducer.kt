package com.setjy.practiceapp.presentation.view.profile

import com.setjy.practiceapp.presentation.view.base.Reducer

class LoadUserReducer : Reducer<State, Action> {
    override fun reduce(state: State, action: Action): State {
        return when (action) {
            is Action.LoadingOwnUser -> state.copy(
                userItemUI = null,
                error = null,
                isLoading = true
            )
            is Action.ErrorLoadOwnUser -> state.copy(
                userItemUI = null,
                error = action.error,
                isLoading = false
            )
            is Action.OwnUserLoaded -> state.copy(
                userItemUI = action.user,
                error = null,
                isLoading = false
            )
        }
    }

}