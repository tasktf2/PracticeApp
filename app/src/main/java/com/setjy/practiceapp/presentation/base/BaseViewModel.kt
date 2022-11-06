package com.setjy.practiceapp.presentation.base

import androidx.lifecycle.ViewModel
import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer

abstract class BaseViewModel<A : BaseAction, S : BaseState>(
    private val reducer: Reducer<A, S>,
    private val middlewares: List<Middleware<S, A>>,
    private val initialState: S
) : ViewModel()