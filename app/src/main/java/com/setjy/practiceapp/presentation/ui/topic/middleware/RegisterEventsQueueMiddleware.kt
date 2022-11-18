package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.data.remote.response.SendEventResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RegisterEventsQueueMiddleware @Inject constructor(
    private val registerEventsQueueUseCase: @JvmSuppressWildcards UseCase<Unit, Observable<SendEventResponse>>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> =
        actions.ofType(TopicAction.RegisterEventsQueue::class.java).flatMap {
            registerEventsQueueUseCase.execute(Unit)
                .map<TopicAction> {
                    TopicAction.QueueRegistered(queueId = it.queueId, lastEventId = it.lastEventId)
                }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
}