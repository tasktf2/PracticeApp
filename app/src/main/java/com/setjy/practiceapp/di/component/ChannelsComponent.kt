package com.setjy.practiceapp.di.component

import com.setjy.practiceapp.di.module.channels.ChannelsBindModule
import com.setjy.practiceapp.di.module.channels.ChannelsModule
import com.setjy.practiceapp.di.module.people.PeopleBindModule
import com.setjy.practiceapp.di.module.people.PeopleModule
import com.setjy.practiceapp.di.scope.ChannelsScope
import com.setjy.practiceapp.di.scope.PeopleScope
import com.setjy.practiceapp.presentation.ui.channels.StreamListFragment
import com.setjy.practiceapp.presentation.ui.people.PeopleFragment
import dagger.Subcomponent
import dagger.Subcomponent.Builder

@ChannelsScope
@Subcomponent(modules = [ChannelsModule::class, ChannelsBindModule::class])
interface ChannelsComponent {

    fun inject(streamListFragment: StreamListFragment)

    @Builder
    interface ChannelsBuilder {
        fun buildChannels(): ChannelsComponent
    }
}