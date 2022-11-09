package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.remote.api.ReactionsApi
import com.setjy.practiceapp.domain.repo.ReactionRepo

class ReactionRepoImpl(private val api: ReactionsApi) : ReactionRepo {

    override fun addReaction(messageId: Int, emojiName: String) =
        api.addReaction(messageId = messageId, emojiName = emojiName)

    override fun deleteReaction(messageId: Int, emojiName: String) =
        api.deleteReaction(messageId = messageId, emojiName = emojiName)
}