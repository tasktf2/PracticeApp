package com.setjy.practiceapp.presentation.base.mvi

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

class Store<A : BaseAction, S : BaseState, E : BaseEffect>(
    private val reducer: Reducer<A, S, E>,
    private val middlewares: List<Middleware<S, A>>,
    initialState: S
) {
    private val state = BehaviorRelay.createDefault(initialState)
    private val actions = PublishRelay.create<A>()
    private val effects = PublishRelay.create<E>()

    fun accept(action: A) {
        actions.accept(action)
    }

    fun wire(): Disposable {
        val disposable = CompositeDisposable()
        disposable += actions.withLatestFrom(state, reducer::reduceToState)
            .distinctUntilChanged()
            .subscribe(state::accept)

        disposable += actions.withLatestFrom(state, reducer::reduceToEffect)
            .filter { it.isPresent }
            .map(Optional<E>::get)
            .subscribe(effects::accept)

        disposable += Observable.merge(middlewares.map { it.bind(actions, state) })
            .subscribe(actions::accept)

        return disposable
    }

    fun bind(view: MviView<S, E>): Disposable {
        val disposable = CompositeDisposable()
        disposable += state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::renderState)
        disposable += effects.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::renderEffect)
        return disposable
    }
}
