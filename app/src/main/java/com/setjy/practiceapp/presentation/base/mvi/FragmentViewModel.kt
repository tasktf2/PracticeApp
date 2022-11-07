package com.setjy.practiceapp.presentation.base.mvi

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable

class FragmentViewModel<A : BaseAction, S : BaseState>(
    private val reducer: Reducer<A, S>,
    private val middlewares: List<Middleware<S, A>>,
    private val initialState: S
) : ViewModel() {

    private val store: Store<A, S> by lazy {
        Store(reducer, middlewares, initialState)
    }

    private val wiring = store.wire()
    private var viewBinding: Disposable? = null

    fun accept(action: A) {
        store.accept(action)
    }

    fun bind(mviView: MviView<S>) {
        viewBinding = store.bind(mviView)
    }

    fun unbind() {
        viewBinding?.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }
}