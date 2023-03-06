package com.setjy.practiceapp.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.BaseViewModel
import com.setjy.practiceapp.presentation.base.Reducer
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.rx3.awaitFirst


class ProfileViewModel(
    private val getOwnUserUseCase: UseCase<Unit, Observable<UserItemUI>>,
    private val profileState: ProfileState
) : BaseViewModel<ProfileAction, ProfileState, ProfileEffect>() {

    override val initialState: ProfileState
        get() = profileState

    private val reducer: Reducer<ProfileState, ProfileAction> = { state, action ->
        when (action) {
            is ProfileAction.ShowLoading -> state.copy(isLoading = true)
            is ProfileAction.ShowOwnUser -> state.copy(
                userItemUI = action.user,
                isLoading = false,
                error = null
            )
            is ProfileAction.ShowError -> state.copy(error = action.error, isLoading = false)
            else -> state
        }
    }

    init {
        bindActionGetOwnUser()
            .scan(profileState, reducer)
            .distinctUntilChanged()
            .catch { Log.d(TAG, it.stackTraceToString()) }
            .onEach(states::emit)
            .launchIn(viewModelScope)
    }

    private fun bindActionGetOwnUser(): Flow<ProfileAction> {
        return actions.filterIsInstance<ProfileAction.LoadOwnUser>().map {
            viewModelScope.async {
                getOwnUserUseCase.execute(Unit).awaitFirst()
            }
        }.map { action ->
            ProfileAction.ShowOwnUser(action.await())
        }.onStart { ProfileAction.ShowLoading }
    }

    companion object {
        private const val TAG: String = "ProfileViewModel"
    }
}