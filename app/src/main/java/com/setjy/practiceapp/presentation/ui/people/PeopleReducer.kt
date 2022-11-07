package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.Reducer

class PeopleReducer : Reducer<PeopleAction, PeopleState> {
    override fun reduce(action: PeopleAction, state: PeopleState): PeopleState {
        return when (action) {
            PeopleAction.LoadUsers -> state.copy(
                isLoading = false, error = null, users = null
            )
            is PeopleAction.ShowError -> state.copy(
                isLoading = false, error = action.error, users = null
            )

            PeopleAction.ShowLoading -> state.copy(
                isLoading = true, error = null, users = null
            )

            is PeopleAction.ShowUsers -> state.copy(
                isLoading = false, error = null, users = action.users
            )
        }
    }
}