package com.setjy.practiceapp.di.module.channels

import com.setjy.practiceapp.data.repo.StreamRepoImpl
import com.setjy.practiceapp.data.repo.TopicRepoImpl
import com.setjy.practiceapp.di.scope.ChannelsScope
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.model.StreamMapper
import com.setjy.practiceapp.domain.model.StreamWithTopics
import com.setjy.practiceapp.domain.repo.StreamRepo
import com.setjy.practiceapp.domain.repo.TopicRepo
import com.setjy.practiceapp.presentation.base.mvi.BaseEffect
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.ui.channels.ChannelsAction
import com.setjy.practiceapp.presentation.ui.channels.ChannelsReducer
import com.setjy.practiceapp.presentation.ui.channels.ChannelsState
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import dagger.Binds
import dagger.Module

@Module
interface ChannelsBindModule {

    @Binds
    @ChannelsScope
    fun bindChannelsReducer(channelsReducer: ChannelsReducer): Reducer<ChannelsAction, ChannelsState, BaseEffect>

    @Binds
    @ChannelsScope
    fun bindTopicRepo(topicRepoImpl: TopicRepoImpl): TopicRepo

    @Binds
    @ChannelsScope
    fun bindStreamRepo(streamRepoImpl: StreamRepoImpl): StreamRepo

    @Binds
    @ChannelsScope
    fun bindStreamsMapper(streamMapper: StreamMapper): DomainMapper<StreamWithTopics, StreamItemUI>
}