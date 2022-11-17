package com.setjy.practiceapp.di.module.profile

import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideProfileState(): ProfileState = ProfileState()
}