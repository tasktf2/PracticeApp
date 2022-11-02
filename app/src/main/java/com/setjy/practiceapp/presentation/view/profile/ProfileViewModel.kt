package com.setjy.practiceapp.presentation.view.profile

import androidx.lifecycle.ViewModel
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.presentation.view.base.MviView
import io.reactivex.rxjava3.disposables.Disposable

class ProfileViewModel : ViewModel() {

    private val store: LoadUserStore by lazy { (ZulipApp.appContext as ZulipApp).globalDI.store }

    private val wiring = store.wire()
    private var viewBinding: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        wiring.dispose()
    }

    fun bind(view: MviView<Action, State>) {
        viewBinding = store.bind(view)
    }

    fun unbind() {
        viewBinding?.dispose()
    }
}