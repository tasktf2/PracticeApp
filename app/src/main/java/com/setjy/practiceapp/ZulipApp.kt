package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.GlobalDI

class ZulipApp : Application() {

    val globalDI: GlobalDI by lazy { GlobalDI() }

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }

    companion object {

        lateinit var appContext: Context
    }
}
