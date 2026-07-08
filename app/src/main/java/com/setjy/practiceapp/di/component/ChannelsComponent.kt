package com.setjy.practiceapp.di.component

import com.setjy.practiceapp.di.module.channels.ChannelsBindModule
import com.setjy.practiceapp.di.module.channels.ChannelsModule
import com.setjy.practiceapp.di.scope.ChannelsScope
import com.setjy.practiceapp.presentation.ui.channels.ChannelsFragment
import dagger.Subcomponent
import dagger.Subcomponent.Builder

@ChannelsScope
@Subcomponent(modules = [ChannelsModule::class, ChannelsBindModule::class])
interface ChannelsComponent {

    fun inject(streamListFragment: ChannelsFragment)

    @Builder
    interface ChannelsBuilder {
        fun buildChannels(): ChannelsComponent
    }
}