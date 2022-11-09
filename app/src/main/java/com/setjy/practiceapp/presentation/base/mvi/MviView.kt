package com.setjy.practiceapp.presentation.base.mvi


interface MviView<in S : BaseState, in E : BaseEffect> {

    fun renderState(state: S)
    fun renderEffect(effect: E)
}