package com.setjy.practiceapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware

class ProfileViewModelFactory : ViewModelProvider.Factory {

    private val getOwnUserUseCase by lazy { (ZulipApp.appContext as ZulipApp).globalDI.getOwnUserUseCase }
    private val reducer by lazy { LoadUserReducer() }
    private val middlewares: List<LoadUserMiddleware> by lazy {
        listOf(LoadUserMiddleware(getOwnUserUseCase))
    }
    private val initialState: ProfileState by lazy { ProfileState() }
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfileViewModel(reducer, middlewares, initialState) as T
}