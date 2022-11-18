package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.GetNewestMessagesUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetNewestMessagesMiddleware @Inject constructor(
    private val getNewestMessagesUseCase: @JvmSuppressWildcards UseCase<GetNewestMessagesUseCase.Params, Flowable<List<MessageUI>>>,
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.GetNewestMessages::class.java).flatMap {
            getNewestMessagesUseCase.execute(
                GetNewestMessagesUseCase.Params(
                    streamName = it.streamName,
                    topicName = it.topicName
                )
            ).toObservable()
                .map<TopicAction> { TopicAction.ShowMessages(it.asReversed()) }
                .onErrorReturn { TopicAction.ShowError(it) }
                .startWithItem(TopicAction.ShowLoading)
        }
    }
}