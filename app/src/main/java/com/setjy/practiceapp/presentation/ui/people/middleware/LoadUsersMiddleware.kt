package com.setjy.practiceapp.presentation.ui.people.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.people.PeopleAction
import com.setjy.practiceapp.presentation.ui.people.PeopleState
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LoadUsersMiddleware @Inject constructor(
    private val getAllUsersUseCase: @JvmSuppressWildcards UseCase<Unit, Observable<List<UserItemUI>>>
) : Middleware<PeopleState, PeopleAction> {
    override fun bind(
        actions: Observable<PeopleAction>,
        state: Observable<PeopleState>
    ): Observable<PeopleAction> {
        return actions.ofType(PeopleAction.LoadUsers::class.java).flatMap {
            getAllUsersUseCase.execute(Unit)
                .map<PeopleAction> { PeopleAction.ShowUsers(it) }
                .onErrorReturn { PeopleAction.ShowError(it) }
                .startWithItem(PeopleAction.ShowLoading)
        }
    }
}