package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.remote.response.UserStatusPresence
import com.setjy.practiceapp.data.remote.response.UsersRemote
import com.setjy.practiceapp.data.remote.response.UsersResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("users")
    fun getAllUsers(): Observable<UsersResponse>

    @GET("users/me")
    fun getOwnUser(): Observable<UsersRemote>

    @GET("users/{user_id_or_email}/presence")
    fun getUserStatus(@Path("user_id_or_email") userId: Int): Observable<UserStatusPresence>
}