package com.setjy.practiceapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.BaseState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<A : BaseAction, S : BaseState, E : BaseEffect> : ViewModel() {

    protected abstract val initialState: S

    protected val actions: MutableSharedFlow<A> = MutableSharedFlow()

    protected val states: MutableSharedFlow<S> = MutableSharedFlow()

    val observableStates: StateFlow<S> by lazy {
        states.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialState
        )
    }

    fun dispatch(action: A) {
        viewModelScope.launch { actions.emit(action) }
    }
}