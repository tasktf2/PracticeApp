package com.setjy.practiceapp.presentation.base.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@Composable
fun <A : BaseAction, S : BaseState, E : BaseEffect> MviViewModel<A, S, E>.subscribe(onEffect: (E) -> Unit): S {
    val state by this.state.subscribeAsState(this.currentState)
    DisposableEffect(this) {
        val disposable = effects.observeOn(AndroidSchedulers.mainThread()).subscribe(onEffect)
        onDispose { disposable.dispose() }
    }

    return state
}