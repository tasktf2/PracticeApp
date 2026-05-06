package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.component.AppComponent
import com.setjy.practiceapp.di.component.ChannelsComponent
import com.setjy.practiceapp.di.component.DaggerAppComponent
import com.setjy.practiceapp.di.component.PeopleComponent
import com.setjy.practiceapp.di.component.ProfileComponent
import com.setjy.practiceapp.di.component.TopicComponent

class ZulipApp : Application() {

    lateinit var appComponent: AppComponent
    var profileComponent: ProfileComponent? = null
    var peopleComponent: PeopleComponent? = null
    var channelsComponent: ChannelsComponent? = null
    var topicComponent: TopicComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .context(this)
            .buildAppComponent()
        appContext = this
    }

    fun addProfileComponent() {
        if (profileComponent == null) {
            profileComponent = appComponent
                .profileBuilder()
                .buildProfile()
        }
    }

    fun addPeopleComponent() {
        if (peopleComponent == null) {
            peopleComponent = appComponent
                .peopleBuilder()
                .buildPeople()
        }
    }

    fun addChannelsComponent() {
        if (channelsComponent == null) {
            channelsComponent = appComponent
                .channelsBuilder()
                .buildChannels()
        }
    }

    fun addTopicComponent() {
        if (topicComponent == null) {
            topicComponent = appComponent
                .topicBuilder()
                .buildTopic()
        }
    }

    fun clearProfileComponent() {
        profileComponent = null
    }

    fun clearPeopleComponent() {
        peopleComponent = null
    }

    fun clearChannelsComponent() {
        channelsComponent = null
    }

    fun clearTopicComponent() {
        topicComponent = null
    }

    companion object {
        lateinit var appContext: Context
    }
}