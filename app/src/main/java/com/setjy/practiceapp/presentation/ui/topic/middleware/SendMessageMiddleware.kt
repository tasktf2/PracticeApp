package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SendMessageMiddleware @Inject constructor(
    private val sendMessageUseCase: @JvmSuppressWildcards UseCase<SendMessageUseCase.Params, Completable>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        var message: String
        return actions.ofType(TopicAction.SendMessage::class.java)
            .withLatestFrom(state) { action, state -> action to state }
            .flatMap { (action, state) ->
                message = state.message.orEmpty()
                sendMessageUseCase.execute(
                    SendMessageUseCase.Params(
                        streamName = action.streamName,
                        topicName = action.topicName,
                        message = state.message.orEmpty()
                    )
                ).toObservable<TopicAction>()
                    .startWithItem(TopicAction.DeleteMessageText)
                    .onErrorReturn { TopicAction.ShowErrorAndReturnMessage(it, message) }
            }
    }
}