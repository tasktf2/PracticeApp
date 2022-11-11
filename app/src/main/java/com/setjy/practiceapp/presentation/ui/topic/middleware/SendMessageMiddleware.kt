package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class SendMessageMiddleware(
    private val sendMessageUseCase: UseCase<SendMessageUseCase.Params, Completable>,
    private val streamName: String,
    private val topicName: String
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.SendMessage::class.java).flatMap { action ->
            sendMessageUseCase.execute(
                SendMessageUseCase.Params(
                    streamName = streamName,
                    topicName = topicName,
                    message = action.message
                )
            ).toObservable<TopicAction>()
                .map<TopicAction> { TopicAction.ActionSent }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }
}