package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.remote.response.TopicsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface TopicsApi {

    @GET("users/me/{stream_id}/topics")
    fun getTopicsById(@Path("stream_id") streamId: Int): Observable<TopicsResponse>
}