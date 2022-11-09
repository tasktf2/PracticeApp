package com.setjy.practiceapp.data.remote.api

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReactionsApi {

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
}