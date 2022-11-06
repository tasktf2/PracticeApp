package com.setjy.practiceapp.presentation.base.mvi


interface MviView<in S> {

    fun render(state: S)
}