package com.setjy.practiceapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware
import io.reactivex.rxjava3.core.Observable

class ProfileViewModelFactory : ViewModelProvider.Factory {

    private val getOwnUserUseCase: UseCase<Unit, Observable<UserItemUI>> by lazy {
        (ZulipApp.appContext as ZulipApp).globalDI.getOwnUserUseCase
    }
    private val reducer by lazy { ProfileReducer() }
    private val middlewares: List<LoadUserMiddleware> by lazy {
        listOf(LoadUserMiddleware(getOwnUserUseCase))
    }
    private val initialState: ProfileState by lazy { ProfileState() }
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MviViewModel(reducer, middlewares, initialState) as T
}