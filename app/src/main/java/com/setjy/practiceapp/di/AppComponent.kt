package com.setjy.practiceapp.di

import android.content.SharedPreferences
import com.setjy.practiceapp.data.remote.api.UsersApi
import dagger.Component
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun sch(): Scheduler
    fun api(): UsersApi
    fun pref(): SharedPreferences
}