package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class SendMessageMiddleware(
    private val sendMessageUseCase: UseCase<SendMessageUseCase.Params, Completable>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.SendMessage::class.java).flatMap { action ->
            sendMessageUseCase.execute(
                SendMessageUseCase.Params(
                    streamName = action.streamName,
                    topicName = action.topicName,
                    message = action.message
                )
            ).toObservable<TopicAction>()
                .ofType(TopicAction::class.java)
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }
}