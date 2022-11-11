package com.setjy.practiceapp.presentation.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.data.remote.response.EmojiToggleResponse
import com.setjy.practiceapp.data.remote.response.SendEventResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.event.GetEventsUseCase
import com.setjy.practiceapp.domain.usecase.message.GetMessagesOnScrollUseCase
import com.setjy.practiceapp.domain.usecase.message.GetNewestMessagesUseCase
import com.setjy.practiceapp.domain.usecase.message.SendMessageUseCase
import com.setjy.practiceapp.domain.usecase.reaction.AddReactionUseCase
import com.setjy.practiceapp.domain.usecase.reaction.DeleteReactionUseCase
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.topic.middleware.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class TopicViewModelFactory(streamName: String, topicName: String) : ViewModelProvider.Factory {

    private val globalDI by lazy { (ZulipApp.appContext as ZulipApp).globalDI }

    private val addReactionUseCase: UseCase<AddReactionUseCase.Params, Single<EmojiToggleResponse>>
            by lazy { globalDI.addReactionUseCase }

    private val deleteReactionUseCase: UseCase<DeleteReactionUseCase.Params, Single<EmojiToggleResponse>>
            by lazy { globalDI.deleteReactionUseCase }

    private val registerEventsQueueUseCase: UseCase<Unit, Observable<SendEventResponse>>
            by lazy { globalDI.registerEventsQueueUseCase }

    private val getEventsUseCase: UseCase<GetEventsUseCase.Params, Observable<List<MessageUI>>>
            by lazy { globalDI.getEventsUseCase }

    private val getMessagesOnScrollUseCase: UseCase<GetMessagesOnScrollUseCase.Params, Observable<List<MessageUI>>>
            by lazy { globalDI.getMessagesOnScrollUseCase }

    private val getNewestMessagesUseCase: UseCase<GetNewestMessagesUseCase.Params, Flowable<List<MessageUI>>>
            by lazy { globalDI.getNewestMessagesUseCase }

    private val sendMessageUseCase: UseCase<SendMessageUseCase.Params, Completable>
            by lazy { globalDI.sendMessageUseCase }


    private val reducer: Reducer<TopicAction, TopicState, TopicEffect> by lazy { TopicReducer() }
    private val middlewares = listOf(
        AddReactionMiddleware(addReactionUseCase),
        DeleteReactionMiddleware(deleteReactionUseCase),
        RegisterEventsQueueMiddleware(registerEventsQueueUseCase),
        GetEventsMiddleware(getEventsUseCase, streamName, topicName),
        GetNewestMessagesMiddleware(getNewestMessagesUseCase, streamName, topicName),
        GetMessagesOnScrollMiddleware(getMessagesOnScrollUseCase, streamName, topicName),
        SendMessageMiddleware(sendMessageUseCase, streamName, topicName)
    )
    private val initialState: TopicState by lazy { TopicState() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MviViewModel(reducer, middlewares, initialState) as T
    }
}