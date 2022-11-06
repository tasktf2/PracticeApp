package com.setjy.practiceapp.presentation.base.mvi

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class Store<A : BaseAction, S : BaseState>(
    private val reducer: Reducer<A, S>,
    private val middlewares: List<Middleware<S, A>>,
    initialState: S
) {
    private val state = BehaviorRelay.createDefault(initialState)
    private val actions = PublishRelay.create<A>()

    fun accept(action: A) {
        actions.accept(action)
    }

    fun wire(): Disposable {
        val disposable = CompositeDisposable()
        disposable += actions.withLatestFrom(state, reducer::reduce)
            .distinctUntilChanged()
            .subscribe(state::accept)

        disposable += Observable.merge(middlewares.map { it.bind(actions, state) })
            .subscribe(actions::accept)

        return disposable
    }

    fun bind(view: MviView<S>): Disposable {
        val disposable = CompositeDisposable()
        disposable += state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::render) //todo scheduler
        return disposable
    }
}
