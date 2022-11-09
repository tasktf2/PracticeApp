package com.setjy.practiceapp.presentation.base.mvi


interface MviView<in S : BaseState> {

    fun render(state: S)
}