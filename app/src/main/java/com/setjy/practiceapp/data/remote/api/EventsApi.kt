package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.remote.response.GetEventResponse
import com.setjy.practiceapp.data.remote.response.SendEventResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventsApi {

    @POST("register")
    fun registerEventQueue(
        @Query("apply_markdown") applyMarkDown: Boolean = false,
        @Query("slim_presence") slimPresence: Boolean = true, //if true will be used user_id instead of email
    ): Observable<SendEventResponse>

    @GET("events")
    fun getEventsQueue(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int
    ): Observable<GetEventResponse>
}