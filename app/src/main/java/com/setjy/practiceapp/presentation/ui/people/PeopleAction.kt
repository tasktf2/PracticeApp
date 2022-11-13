package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI

sealed class PeopleAction : BaseAction {

    object LoadUsers : PeopleAction()

    object ShowLoading : PeopleAction()

    data class ShowUsers(val users: List<UserItemUI>) : PeopleAction()

    data class ShowError(val error: Throwable) : PeopleAction()

    data class SearchUsers(val query: String) : PeopleAction()

    data class ShowSearchResult(val visibleUsers: List<UserItemUI>) : PeopleAction()
}