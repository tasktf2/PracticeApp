package com.setjy.practiceapp

import android.app.Application
import android.content.Context

class ZulipApp : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }

    companion object {
        
        lateinit var appContext: Context
    }
}
