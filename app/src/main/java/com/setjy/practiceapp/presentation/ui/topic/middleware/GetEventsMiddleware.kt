package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.event.GetEventsUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable

class GetEventsMiddleware(
    private val getEventsUseCase: UseCase<GetEventsUseCase.Params, Observable<Pair<List<MessageUI>, Int>>>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.GetEvents::class.java).flatMap { action ->
            getEventsUseCase.execute(
                GetEventsUseCase.Params(
                    streamName = action.streamName,
                    topicName = action.topicName,
                    queueId = action.queueId,
                    lastEventId = action.lastEventId
                )
            )
                .map<TopicAction> { (messages, eventId) ->
                    TopicAction.ShowEvents(
                        messages.asReversed(),
                        queueId = action.queueId,
                        lastEventId = eventId
                    )
                }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }
}