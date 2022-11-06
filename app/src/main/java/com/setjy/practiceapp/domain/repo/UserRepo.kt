package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.data.remote.response.UsersRemote
import com.setjy.practiceapp.presentation.ui.profile.UserStatus
import io.reactivex.rxjava3.core.Observable

interface UserRepo {
    fun getOwnUser(): Observable<UsersRemote>

    fun getUserStatus(userId: Int): Observable<UserStatus>

    fun getAllUsers(): Observable<List<UsersRemote>>
}