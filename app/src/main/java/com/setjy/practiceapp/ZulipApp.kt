package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.component.*

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