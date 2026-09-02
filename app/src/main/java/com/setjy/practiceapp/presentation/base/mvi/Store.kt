package com.setjy.practiceapp.presentation.base.mvi

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.setjy.practiceapp.util.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.Optional
import javax.inject.Inject

class Store<A : BaseAction, S : BaseState, E : BaseEffect> @Inject constructor(
    private val reducer: @JvmSuppressWildcards Reducer<A, S, E>,
    private val middlewares: @JvmSuppressWildcards Set<Middleware<S, A>>,
    initialState: S
) {
    private val _state = BehaviorRelay.createDefault(initialState)
    val state = _state.hide()

    private val _actions = PublishRelay.create<A>()
    private val _effects = PublishRelay.create<E>()

    val effects: Observable<E> = _effects.hide()

    val currentState: S
        get() = _state.value!!

    fun accept(action: A) {
        _actions.accept(action)
    }

    fun wire(): Disposable {
        val disposable = CompositeDisposable()
        disposable += _actions.withLatestFrom(_state, reducer::reduceToState)
            .distinctUntilChanged()
            .subscribe(_state::accept)

        disposable += _actions.withLatestFrom(_state, reducer::reduceToEffect)
            .filter { it.isPresent }
            .map(Optional<E>::get)
            .subscribe(_effects::accept)

        disposable += Observable.merge(middlewares.map { it.bind(_actions, _state) })
            .subscribe(_actions::accept)

        return disposable
    }

    fun bind(view: MviView<S, E>): Disposable {
        val disposable = CompositeDisposable()
        disposable += _state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::renderState)
        disposable += _effects.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::renderEffect)
        return disposable
    }
}
