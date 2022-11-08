package com.setjy.practiceapp.presentation.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.mvi.FragmentViewModel
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.ui.people.middleware.LoadUsersMiddleware
import com.setjy.practiceapp.presentation.ui.people.middleware.SearchUsersMiddleware
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import io.reactivex.rxjava3.core.Observable

class PeopleViewModelFactory : ViewModelProvider.Factory {

    private val getAllUsersUseCase: UseCase<Unit, Observable<List<UserItemUI>>> by lazy {
        (ZulipApp.appContext as ZulipApp).globalDI.getAllUsersUseCase
    }
    private val reducer: Reducer<PeopleAction, PeopleState> by lazy { PeopleReducer() }
    private val middlewares: List<Middleware<PeopleState, PeopleAction>> by lazy {
        listOf(
            LoadUsersMiddleware(getAllUsersUseCase),
            SearchUsersMiddleware()
        )
    }
    private val initialState: PeopleState = PeopleState()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentViewModel(reducer, middlewares, initialState) as T
    }
}