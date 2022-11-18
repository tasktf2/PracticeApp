package com.setjy.practiceapp.presentation.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class MviViewModelFactory<A : BaseAction, S : BaseState, E : BaseEffect> @Inject constructor(
    private val store: Store<A, S, E>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.getConstructor(Store::class.java).newInstance(store)
}