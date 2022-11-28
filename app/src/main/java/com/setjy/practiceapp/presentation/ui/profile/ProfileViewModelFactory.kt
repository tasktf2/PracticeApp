package com.setjy.practiceapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.domain.base.UseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val getOwnUserUseCase: @JvmSuppressWildcards UseCase<Unit, Observable<UserItemUI>>,
    private val initialState: ProfileState
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfileViewModel(getOwnUserUseCase, initialState) as T
}