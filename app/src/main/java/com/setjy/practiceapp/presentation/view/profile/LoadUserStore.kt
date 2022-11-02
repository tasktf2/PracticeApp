package com.setjy.practiceapp.presentation.view.profile

import android.util.Log
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.setjy.practiceapp.presentation.view.base.MviView
import com.setjy.practiceapp.presentation.view.base.Store
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class LoadUserStore(
    private val reducer: LoadUserReducer,
    private val middlewares: List<LoadUserMiddleware>,
    initialState: State
) : Store<Action, State>(reducer, middlewares, initialState) {

    private val state = BehaviorRelay.createDefault(initialState)
    private val actions = PublishRelay.create<Action>()

    override fun wire(): Disposable {
        val disposable = CompositeDisposable()
        disposable += actions.withLatestFrom(state) { action, state ->
            reducer.reduce(state, action)
        }
            .distinctUntilChanged()
            .subscribe(state::accept)

        disposable += Observable.merge(middlewares.map { it.bind(actions, state) })
            .subscribe(actions::accept)

        return disposable
    }

    override fun bind(view: MviView<Action, State>): Disposable {
        val disposable = CompositeDisposable()
        disposable += state.observeOn(AndroidSchedulers.mainThread()).subscribe(view::render)
        disposable += view.actions.subscribe(actions::accept)
        return disposable
    }
}