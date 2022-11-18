package com.setjy.practiceapp.presentation.ui.channels.middleware

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LoadStreamsMiddleware @Inject constructor(
    private val getStreamsUseCase: @JvmSuppressWildcards UseCase<GetStreamsUseCase.Params, Flowable<List<StreamItemUI>>>
) :
    Middleware<ChannelsState, ChannelsAction> {
    override fun bind(
        actions: Observable<ChannelsAction>,
        state: Observable<ChannelsState>
    ): Observable<ChannelsAction> {
        return actions.ofType(ChannelsAction.LoadStreams::class.java)
            .flatMap { action ->
                getStreamsUseCase.execute(GetStreamsUseCase.Params(action.isSubscribed))
                    .map<ChannelsAction> { ChannelsAction.ShowStreams(it) }
                    .onErrorReturn { ChannelsAction.ShowError(it) }
                    .startWithItem(ChannelsAction.ShowLoading)
                    .toObservable()
            }
    }
}