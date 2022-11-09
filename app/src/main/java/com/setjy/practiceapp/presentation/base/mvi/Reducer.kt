package com.setjy.practiceapp.presentation.base.mvi

interface Reducer<in A : BaseAction, S : BaseState> {
    fun reduce(action: A, state: S): S
}