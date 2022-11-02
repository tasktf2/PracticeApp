package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.remote.api.UsersApi
import com.setjy.practiceapp.data.remote.response.UsersRemote
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.presentation.model.UserStatus
import io.reactivex.rxjava3.core.Observable

class UserRepoImpl constructor(private val api: UsersApi) : UserRepo {

    override fun getOwnUser(): Observable<UsersRemote> = api.getOwnUser()

    override fun getUserStatus(userId: Int): Observable<UserStatus> =
        api.getUserStatus(userId = userId)
            .map { response -> response.presence.statusAndTimestamp.status }

    override fun getAllUsers(): Observable<List<UsersRemote>> =
        api.getAllUsers().map { response -> response.members }
}