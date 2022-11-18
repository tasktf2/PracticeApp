package com.setjy.practiceapp.di.module

import com.setjy.practiceapp.di.component.ChannelsComponent
import com.setjy.practiceapp.di.component.PeopleComponent
import com.setjy.practiceapp.di.component.ProfileComponent
import com.setjy.practiceapp.di.component.TopicComponent
import dagger.Module

@Module(
    subcomponents = [
        ProfileComponent::class,
        PeopleComponent::class,
        ChannelsComponent::class,
        TopicComponent::class
    ]
)
class AppModule