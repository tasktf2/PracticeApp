package com.setjy.practiceapp.presentation.view.base

import com.setjy.practiceapp.presentation.view.profile.State

interface Reducer<S, A> {
    fun reduce(state: S, action: A): S
}