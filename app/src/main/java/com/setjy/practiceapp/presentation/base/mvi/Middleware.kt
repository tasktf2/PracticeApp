package com.setjy.practiceapp.presentation.base.mvi

import io.reactivex.rxjava3.core.Observable

interface Middleware<S : BaseState, A : BaseAction> {
    fun bind(actions: Observable<A>, state: Observable<S>): Observable<A>
}