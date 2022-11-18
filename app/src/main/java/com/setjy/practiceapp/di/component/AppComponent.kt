package com.setjy.practiceapp.di.component

import android.content.Context
import android.content.SharedPreferences
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.di.module.AppModule
import com.setjy.practiceapp.di.module.NetworkModule
import com.setjy.practiceapp.di.module.RepoModule
import com.setjy.practiceapp.di.module.RoomModule
import com.setjy.practiceapp.di.scope.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder
import io.reactivex.rxjava3.core.Scheduler

@AppScope
@Component(modules = [AppModule::class, NetworkModule::class, RepoModule::class, RoomModule::class])
interface AppComponent {

    fun profileBuilder(): ProfileComponent.ProfileBuilder
    fun peopleBuilder(): PeopleComponent.PeopleBuilder
    fun channelsBuilder(): ChannelsComponent.ChannelsBuilder
    fun topicBuilder(): TopicComponent.TopicBuilder

    fun scheduler(): Scheduler
    fun preferences(): SharedPreferences
    fun db(): ZulipDatabase

    @Builder
    interface AppBuilder {

        @BindsInstance
        fun context(context: Context): AppBuilder

        fun buildAppComponent(): AppComponent
    }
}