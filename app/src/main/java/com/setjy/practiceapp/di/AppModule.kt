package com.setjy.practiceapp.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    fun provideScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SHARED_PREFS = "ZULIP_APP_SHARED_PREFS"
    }
}