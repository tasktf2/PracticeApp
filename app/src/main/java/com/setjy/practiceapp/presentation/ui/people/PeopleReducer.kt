package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.Reducer
import java.util.*
import javax.inject.Inject

class PeopleReducer @Inject constructor() : @JvmSuppressWildcards Reducer<PeopleAction, PeopleState, PeopleEffect> {
    override fun reduceToState(action: PeopleAction, state: PeopleState): PeopleState {
        return when (action) {

            is PeopleAction.ShowError -> state.copy(isLoading = false, error = action.error)

            PeopleAction.ShowLoading -> state.copy(isLoading = true)

            is PeopleAction.ShowUsers -> state.copy(
                isLoading = false, users = action.users, visibleUsers = action.users
            )

            is PeopleAction.ShowSearchResult -> state.copy(visibleUsers = action.visibleUsers)
            else -> state
        }
    }

    override fun reduceToEffect(action: PeopleAction, state: PeopleState): Optional<PeopleEffect> {
        return Optional.empty()
    }
}