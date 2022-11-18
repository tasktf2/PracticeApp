package com.setjy.practiceapp.presentation.base.mvi

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class MviViewModel<A : BaseAction, S : BaseState, E : BaseEffect> @Inject constructor(
    private val store: Store<A, S, E>
) : ViewModel() {

    private val wiring = store.wire()
    private var viewBinding: Disposable? = null


    val currentState: S
        get() = store.currentState

    fun accept(action: A) {
        store.accept(action)
    }

    fun bind(mviView: MviView<S, E>) {
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