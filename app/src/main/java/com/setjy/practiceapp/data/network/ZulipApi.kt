package com.setjy.practiceapp.data.network

import com.setjy.practiceapp.data.network.response.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ZulipApi {

    @GET("streams")
    fun getAllStreams(
        @Query("include_public") includePublic: Boolean = true
    ): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Single<StreamsResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopicsById(@Path("stream_id") streamId: Int): Observable<TopicsResponse>

    @GET("users/me")
    fun getOwnUser(): Observable<UsersRemote>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String? = null,
        @Query("apply_markdown") applyMarkDown: Boolean = false
    ): Observable<MessagesResponse>

    @GET("users")
    fun getAllUsers(): Observable<UsersResponse>

    @GET("users/{user_id_or_email}/presence")
    fun getUserStatus(@Path("user_id_or_email") userId: Int): Observable<UserStatusPresence>

    @POST("messages/{message_id}/reactions")
    fun addReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String
    ): Single<EmojiToggleResponse>

    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id") messageId: Int,
        @Query("emoji_name") emojiName: String
    ): Single<EmojiToggleResponse>

    @POST("messages")
    fun sendMessage(
        @Query("to") streamName: String,
        @Query("topic") topicName: String,
        @Query("content") message: String,
        @Query("type") typeOfMessage: String = "stream"
    ): Completable

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