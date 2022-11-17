package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.GlobalDI
import com.setjy.practiceapp.di.component.AppComponent
import com.setjy.practiceapp.di.component.DaggerAppComponent
import com.setjy.practiceapp.di.component.ProfileComponent

class ZulipApp : Application() {

    val globalDI: GlobalDI by lazy { GlobalDI }

    lateinit var appComponent: AppComponent
    var profileComponent: ProfileComponent? = null

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

    fun clearProfileComponent() {
        profileComponent = null
    }

    companion object {

        lateinit var appContext: Context
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is ZulipApp -> appComponent
        else -> this.applicationContext.appComponent
    }