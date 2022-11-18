package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.message.PaginationUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PaginationMiddleware @Inject constructor(
    private val paginationUseCase: @JvmSuppressWildcards UseCase<PaginationUseCase.Params, Observable<List<MessageUI>>>
) :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.StartPagination::class.java).flatMap { action ->
            paginationUseCase.execute(
                PaginationUseCase.Params(
                    streamName = action.streamName,
                    topicName = action.topicName,
                    anchor = action.anchor.toString()
                )
            )
                .map<TopicAction> { messages ->
                    TopicAction.ShowPaginationResult(messages
                        .filterNot { it.messageId == action.anchor }
                        .asReversed(),
                        isLastPage = messages.size < MESSAGES_TO_LOAD)
                }
                .onErrorReturn { TopicAction.ShowError(it) }
        }
    }

    companion object {
        const val MESSAGES_TO_LOAD: Int = 20
    }
}