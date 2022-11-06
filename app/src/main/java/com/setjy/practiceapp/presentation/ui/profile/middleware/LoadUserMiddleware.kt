package com.setjy.practiceapp.presentation.ui.profile.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.profile.ProfileAction
import com.setjy.practiceapp.presentation.ui.profile.ProfileAction.ShowLoading
import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import io.reactivex.rxjava3.core.Observable

class LoadUserMiddleware(private val getOwnUserUseCase: UseCase<Unit, Observable<UserItemUI>>) :
    Middleware<ProfileState, ProfileAction> {
    override fun bind(
        actions: Observable<ProfileAction>,
        state: Observable<ProfileState>
    ): Observable<ProfileAction> =
        actions.ofType(ProfileAction.LoadOwnUser::class.java)
            .flatMap {
                getOwnUserUseCase.execute(Unit)
                    .map<ProfileAction>(ProfileAction::ShowOwnUser)
                    .onErrorReturn(ProfileAction::ShowError)
                    .startWithItem(ShowLoading)
            }
}