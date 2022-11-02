package com.setjy.practiceapp.domain.usecase.reaction

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.repo.ReactionRepo
import io.reactivex.rxjava3.core.Single

class AddReactionUseCase constructor(private val repo: ReactionRepo) :
    UseCase<AddReactionUseCase.Params, Single<EmojiToggleResponse>> {

    data class Params(val messageId: Int, val emojiName: String)

    override fun execute(params: Params?): Single<EmojiToggleResponse> =
        repo.addReaction(messageId = params!!.messageId, emojiName = params.emojiName)
}