package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.reaction.AddReactionUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class AddReactionMiddleware(
    private val addReactionUseCase: UseCase<AddReactionUseCase.Params, Single<EmojiToggleResponse>>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.AddReaction::class.java).flatMap { action ->
            addReactionUseCase.execute(
                AddReactionUseCase.Params(
                    messageId = action.messageId,
                    emojiName = action.emojiName
                )
            ).toObservable()
                .ofType(TopicAction::class.java)
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }
}