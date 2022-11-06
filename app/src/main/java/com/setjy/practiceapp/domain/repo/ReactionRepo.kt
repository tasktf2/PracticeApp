package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import io.reactivex.rxjava3.core.Single

interface ReactionRepo {
    fun addReaction(messageId: Int, emojiName: String): Single<EmojiToggleResponse>

    fun deleteReaction(messageId: Int, emojiName: String): Single<EmojiToggleResponse>
}