package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.Reducer

class PeopleReducer : Reducer<PeopleAction, PeopleState> {
    override fun reduce(action: PeopleAction, state: PeopleState): PeopleState {
        return when (action) {
            is PeopleAction.LoadUsers, is PeopleAction.SearchUsers -> state

            is PeopleAction.ShowError -> state.copy(isLoading = false, error = action.error)

            PeopleAction.ShowLoading -> state.copy(isLoading = true)

            is PeopleAction.ShowUsers -> state.copy(
                isLoading = false, users = action.users, visibleUsers = action.users
            )

            is PeopleAction.ShowSearchResult -> state.copy(visibleUsers = action.visibleUsers)
        }
    }
}