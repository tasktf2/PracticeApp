package com.setjy.practiceapp

import android.app.Application
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class ZulipApp : Application() {

    override fun onCreate() {
        super.onCreate()

        preferences = getSharedPreferences("xxx", MODE_PRIVATE)
        editor = preferences.edit()
    }

    companion object {

        lateinit var preferences: SharedPreferences

        lateinit var editor: Editor
    }
}
