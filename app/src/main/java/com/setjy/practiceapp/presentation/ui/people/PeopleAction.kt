package com.setjy.practiceapp.presentation.ui.people

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI

sealed class PeopleAction : BaseAction {

    object LoadUsers : PeopleAction()

    object ShowLoading : PeopleAction()

    class ShowUsers(val users: List<UserItemUI>) : PeopleAction()

    class ShowError(val error: Throwable) : PeopleAction()

    class SearchUsers(val query: String) : PeopleAction()

    class ShowSearchResult(val visibleUsers: List<UserItemUI>) : PeopleAction()
}