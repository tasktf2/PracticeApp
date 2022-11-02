package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.Constants
import com.setjy.practiceapp.data.remote.response.MessagesResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String? = null,
        @Query("apply_markdown") applyMarkDown: Boolean = false
    ): Observable<MessagesResponse>

    @POST("messages")
    fun sendMessage(
        @Query("to") streamName: String,
        @Query("topic") topicName: String,
        @Query("content") message: String,
        @Query("type") typeOfMessage: String = Constants.MESSAGE_SEND_TYPE
    ): Completable
}