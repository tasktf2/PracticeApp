package com.setjy.practiceapp.di.component

import com.setjy.practiceapp.di.module.topic.TopicBindModule
import com.setjy.practiceapp.di.module.topic.TopicModule
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.di.scope.TopicScope
import com.setjy.practiceapp.presentation.ui.topic.TopicFragment
import dagger.Subcomponent
import dagger.Subcomponent.Builder

@TopicScope
@Subcomponent(modules = [TopicModule::class, TopicBindModule::class])
interface TopicComponent {

    fun inject(topicFragment: TopicFragment)

    @Builder
    interface TopicBuilder {
        fun buildTopic(): TopicComponent
    }
}