package com.setjy.practiceapp.presentation.ui.people.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.people.PeopleAction
import com.setjy.practiceapp.presentation.ui.people.PeopleState
import io.reactivex.rxjava3.core.Observable

class SearchUsersMiddleware : Middleware<PeopleState, PeopleAction> {
    override fun bind(
        actions: Observable<PeopleAction>,
        state: Observable<PeopleState>
    ): Observable<PeopleAction> {
        return actions.ofType(PeopleAction.SearchUsers::class.java)
            .withLatestFrom(state) { action, lastState ->
                PeopleAction.ShowSearchResult(lastState.users.orEmpty().filter {
                    it.fullName.contains(action.query, ignoreCase = true)
                })
            }
    }
}