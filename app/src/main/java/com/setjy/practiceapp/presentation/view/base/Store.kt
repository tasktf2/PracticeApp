package com.setjy.practiceapp.presentation.view.base

import com.setjy.practiceapp.presentation.view.profile.Action
import com.setjy.practiceapp.presentation.view.profile.State
import io.reactivex.rxjava3.disposables.Disposable

abstract class Store<A, S>(
    private val reducer: Reducer<S, A>,
    private val middleware: List<Middleware<S, A>>,
    private val initialState: S

) {
    abstract fun wire(): Disposable
    abstract fun bind(view: MviView<Action, State>): Disposable

}
