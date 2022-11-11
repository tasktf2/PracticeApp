package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.reaction.DeleteReactionUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class DeleteReactionMiddleware(
    private val deleteReactionUseCase: UseCase<DeleteReactionUseCase.Params, Single<EmojiToggleResponse>>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.DeleteReaction::class.java).flatMap { action ->
            deleteReactionUseCase.execute(
                DeleteReactionUseCase.Params(
                    messageId = action.messageId,
                    emojiName = action.emojiName
                )
            ).toObservable()
                .map<TopicAction> { TopicAction.ActionSent }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }
}