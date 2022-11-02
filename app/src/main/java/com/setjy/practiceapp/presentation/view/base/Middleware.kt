package com.setjy.practiceapp.presentation.view.base

import io.reactivex.rxjava3.core.Observable

interface Middleware<S, A> {
    fun bind(actions: Observable<A>, state: Observable<S>): Observable<A>
}