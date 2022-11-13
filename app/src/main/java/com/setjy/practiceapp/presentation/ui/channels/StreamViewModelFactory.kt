package com.setjy.practiceapp.presentation.ui.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setjy.practiceapp.ZulipApp
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.ui.channels.middleware.LoadStreamsMiddleware
import com.setjy.practiceapp.presentation.ui.channels.middleware.SearchStreamsMiddleware
import com.setjy.practiceapp.presentation.ui.channels.middleware.StreamToggleMiddleware
import io.reactivex.rxjava3.core.Flowable

class StreamViewModelFactory : ViewModelProvider.Factory {

    private val getStreamsUseCase: UseCase<GetStreamsUseCase.Params, Flowable<List<StreamItemUI>>> by lazy {
        (ZulipApp.appContext as ZulipApp).globalDI.getStreamsUseCase
    }

    private val reducer: Reducer<ChannelsAction, ChannelsState, BaseEffect> by lazy { ChannelsReducer() }
    private val middlewares: List<Middleware<ChannelsState, ChannelsAction>> by lazy {
        listOf(
            LoadStreamsMiddleware(getStreamsUseCase),
            SearchStreamsMiddleware(),
            StreamToggleMiddleware()
        )
    }
    private val initialState: ChannelsState by lazy { ChannelsState() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MviViewModel(reducer, middlewares, initialState) as T
    }
}