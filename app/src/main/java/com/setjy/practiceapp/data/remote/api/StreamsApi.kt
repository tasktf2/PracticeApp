package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.remote.response.StreamsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface StreamsApi {

    @GET("streams")
    fun getAllStreams(
        @Query("include_public") includePublic: Boolean = true
    ): Observable<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Observable<StreamsResponse>
}