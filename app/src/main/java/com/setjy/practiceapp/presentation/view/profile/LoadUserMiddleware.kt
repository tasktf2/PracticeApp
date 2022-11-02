package com.setjy.practiceapp.presentation.view.profile

import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import com.setjy.practiceapp.presentation.view.base.Middleware
import io.reactivex.rxjava3.core.Observable

class LoadUserMiddleware(private val getOwnUserUseCase: GetOwnUserUseCase) :
    Middleware<State, Action> {
    override fun bind(actions: Observable<Action>, state: Observable<State>): Observable<Action> =
        actions.filter { it is Action.LoadingOwnUser }
            .flatMap {
                getOwnUserUseCase.execute()
                    .map<Action> { result -> Action.OwnUserLoaded(result) }
                    .onErrorReturn { e -> Action.ErrorLoadOwnUser(e) }
            }
}