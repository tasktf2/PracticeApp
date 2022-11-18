package com.setjy.practiceapp.di.module.channels

import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.local.db.dao.StreamDao
import com.setjy.practiceapp.data.local.db.dao.TopicDao
import com.setjy.practiceapp.data.remote.api.StreamsApi
import com.setjy.practiceapp.data.remote.api.TopicsApi
import com.setjy.practiceapp.di.scope.ChannelsScope
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.stream.GetStreamsUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import com.setjy.practiceapp.presentation.ui.channels.middleware.LoadStreamsMiddleware
import com.setjy.practiceapp.presentation.ui.channels.middleware.SearchStreamsMiddleware
import com.setjy.practiceapp.presentation.ui.channels.middleware.StreamToggleMiddleware
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Retrofit

@Module
class ChannelsModule {

    @Provides
    @ChannelsScope
    fun provideStreamsApi(retrofit: Retrofit): StreamsApi = retrofit.create(StreamsApi::class.java)

    @Provides
    @ChannelsScope
    fun provideTopicsApi(retrofit: Retrofit): TopicsApi = retrofit.create(TopicsApi::class.java)

    @Provides
    @ChannelsScope
    fun provideStreamDao(zulipDatabase: ZulipDatabase): StreamDao = zulipDatabase.streamDao()

    @Provides
    @ChannelsScope
    fun provideTopicDao(zulipDatabase: ZulipDatabase): TopicDao = zulipDatabase.topicDao()

    @Provides
    fun provideChannelsInitialState(): ChannelsState = ChannelsState()

    @Provides
    @ChannelsScope
    fun getStreamsUseCase(getStreamsUseCase: GetStreamsUseCase): UseCase<GetStreamsUseCase.Params, Flowable<List<StreamItemUI>>> =
        getStreamsUseCase

    @Provides
    @ChannelsScope
    @ElementsIntoSet
    fun provideChannelsMiddlewares(
        loadStreamsMiddleware: LoadStreamsMiddleware,
        searchStreamsMiddleware: SearchStreamsMiddleware,
        streamToggleMiddleware: StreamToggleMiddleware
    ): Set<Middleware<ChannelsState, ChannelsAction>> =
        setOf(loadStreamsMiddleware, searchStreamsMiddleware, streamToggleMiddleware)
}