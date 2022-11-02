package com.setjy.practiceapp.presentation.view.base

import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Observable

interface MviView<A, S> {

    val actions: Observable<A>
    val _actions: PublishRelay<A>
    fun render(state: S)
}