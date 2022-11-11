package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.GetMessagesOnScrollUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable

class GetMessagesOnScrollMiddleware(
    private val getMessagesOnScrollUseCase: UseCase<GetMessagesOnScrollUseCase.Params, Observable<List<MessageUI>>>,
    private val streamName: String,
    private val topicName: String
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.GetMessagesOnScroll::class.java).flatMap { action ->
            getMessagesOnScrollUseCase.execute(
                GetMessagesOnScrollUseCase.Params(
                    streamName = streamName,
                    topicName = topicName,
                    anchor = action.anchor.toString()
                )
            )
                .map<TopicAction> { messages ->
                    TopicAction.ShowMessagesOnScroll(messages
                        .filterNot { it.messageId == action.anchor }
                        .asReversed(),
                        onScrollIsLoading = false,
                        onScrollIsLastPage = messages.size < MESSAGES_TO_LOAD)
                }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }

    companion object {
        const val MESSAGES_TO_LOAD: Int = 20
    }
}