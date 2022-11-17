package com.setjy.practiceapp.di.module

import com.setjy.practiceapp.di.component.ProfileComponent
import dagger.Module

@Module(
    includes = [NetworkModule::class, RepoModule::class], subcomponents = [ProfileComponent::class]
)
class AppModule