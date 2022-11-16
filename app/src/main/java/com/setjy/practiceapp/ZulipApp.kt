package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.AppComponent
import com.setjy.practiceapp.di.AppModule
import com.setjy.practiceapp.di.DaggerAppComponent
import com.setjy.practiceapp.di.GlobalDI
import com.setjy.practiceapp.di.profile.DaggerProfileComponent
import com.setjy.practiceapp.di.profile.ProfileComponent

class ZulipApp : Application() {

    val globalDI: GlobalDI by lazy { GlobalDI }

    lateinit var appComponent: AppComponent
    var profileComponent: ProfileComponent? = null

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appContext = this
    }

    fun addProfileComponent() {
        if (profileComponent == null) {
            profileComponent = DaggerProfileComponent.builder()
                .appComponent(appComponent)
                .build()
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
