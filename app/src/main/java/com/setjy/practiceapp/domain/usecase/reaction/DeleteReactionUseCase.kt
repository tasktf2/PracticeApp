package com.setjy.practiceapp.domain.usecase.reaction

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.repo.ReactionRepo
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DeleteReactionUseCase @Inject constructor(private val repo: ReactionRepo, private val scheduler: Scheduler) :
    UseCase<DeleteReactionUseCase.Params, Single<EmojiToggleResponse>> {

    data class Params(val messageId: Int, val emojiName: String)

    override fun execute(params: Params): Single<EmojiToggleResponse> =
        repo.deleteReaction(messageId = params.messageId, emojiName = params.emojiName)
            .subscribeOn(scheduler)
}