package com.setjy.practiceapp.di.module.profile

import com.setjy.practiceapp.data.remote.api.UsersApi
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    fun provideProfileInitialState(): ProfileState = ProfileState()
}