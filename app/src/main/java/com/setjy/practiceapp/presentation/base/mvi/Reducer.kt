package com.setjy.practiceapp.presentation.base.mvi

import java.util.Optional

interface Reducer<in A : BaseAction, S : BaseState, E : BaseEffect> {
    fun reduceToState(action: A, state: S): S
    fun reduceToEffect(action: A, state: S): Optional<E>
}