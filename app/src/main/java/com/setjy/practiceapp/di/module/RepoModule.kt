package com.setjy.practiceapp.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SHARED_PREFS = "ZULIP_APP_SHARED_PREFS"
    }
}