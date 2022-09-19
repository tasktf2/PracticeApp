package com.setjy.practiceapp.data.network

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ZulipApi {

    @GET("streams")
    fun getAllStreams(
        @Query("include_public") includePublic: Boolean = true
    ): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun getSubscribedStreams(): Single<StreamsSubscribedResponse>

    @GET("users/me/{stream_id}/topics")
    fun getTopicsById(@Path("stream_id") streamId: Int): Single<TopicsResponse>

    @GET("users/me")
    fun getOwnUser(): Single<UserOwnResponse>

    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String? = null,
        @Query("apply_markdown") applyMarkDown: Boolean = false
    ): Single<MessagesResponse>

    @GET("users")
    fun getAllUsers(): Single<UsersResponse>

    @GET("users/{user_id_or_email}/presence")
    fun getUserStatus(@Path("user_id_or_email") userId: Int): Single<UserStatusPresence>

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
    ): Single<MessageSendResponse>

    @POST("register")
    fun registerEventQueue(
        @Query("apply_markdown") applyMarkDown: Boolean = false,
        @Query("slim_presence") slimPresence: Boolean = true, //if true will be used user_id instead of email
    ): Single<SendEventResponse>

    @GET("events")
    fun getEventsQueue(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int
    ): Observable<GetEventResponse>
}