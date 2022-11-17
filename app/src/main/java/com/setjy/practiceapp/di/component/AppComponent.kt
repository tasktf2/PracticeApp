package com.setjy.practiceapp.di.component

import android.content.Context
import android.content.SharedPreferences
import com.setjy.practiceapp.data.remote.api.UsersApi
import com.setjy.practiceapp.di.module.AppModule
import com.setjy.practiceapp.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {

    fun profileBuilder(): ProfileComponent.ProfileBuilder

    fun scheduler(): Scheduler
    fun api(): UsersApi
    fun preferences(): SharedPreferences

    @Builder
    interface AppBuilder {

        @BindsInstance
        fun context(context: Context): AppBuilder

        fun buildAppComponent(): AppComponent
    }
}