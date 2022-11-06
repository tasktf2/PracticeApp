package com.setjy.practiceapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import com.setjy.practiceapp.presentation.base.mvi.MviView
import com.setjy.practiceapp.presentation.base.mvi.Store
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware
import io.reactivex.rxjava3.disposables.Disposable

class ProfileViewModel(
    private val reducer: LoadUserReducer,
    private val middlewares: List<LoadUserMiddleware>,
    private val initialState: ProfileState
) : ViewModel() {

    private val store: Store<ProfileAction, ProfileState> by lazy {
        Store(reducer, middlewares, initialState)
    }
    private val wiring = store.wire()
    private var viewBinding: Disposable? = null

    fun accept(action: ProfileAction) {
        store.accept(action)
    }

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<ProfileState>) {
        viewBinding = store.bind(view)
    }

    fun unbind() {
        viewBinding?.dispose()
    }
}