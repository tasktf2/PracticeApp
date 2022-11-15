package com.setjy.practiceapp.di

import android.content.Context
import android.content.SharedPreferences
import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.profile.ProfileAction
import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, AppBindModule::class])
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    fun provideScheduler(): Scheduler = Schedulers.io()

    @Provides
    fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    @Provides
    fun provideProfileState(): ProfileState =
        ProfileState(userItemUI = null, error = null, isLoading = false)

    companion object {
        private const val KEY_SHARED_PREFS = "ZULIP_APP_SHARED_PREFS"
    }
}