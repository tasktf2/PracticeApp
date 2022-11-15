package com.setjy.practiceapp

import android.app.Application
import android.content.Context
import com.setjy.practiceapp.di.AppComponent
import com.setjy.practiceapp.di.AppModule
import com.setjy.practiceapp.di.DaggerAppComponent
import com.setjy.practiceapp.di.GlobalDI

class ZulipApp: Application() {

    val globalDI: GlobalDI by lazy { GlobalDI }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
//            .builder().appModule(AppModule(this)).build()
        appContext = this
    }

    companion object {

        lateinit var appContext: Context
    }
}

val Context.appComponent:AppComponent
    get() = when (this) {
        is ZulipApp -> appComponent
        else-> this.applicationContext.appComponent
    }
