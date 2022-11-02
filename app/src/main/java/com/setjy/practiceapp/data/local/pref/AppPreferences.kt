package com.setjy.practiceapp.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import com.setjy.practiceapp.ZulipApp

class AppPreferences
//constructor(private val context: Context)
    : Preferences {

    private val sharedPreferences: SharedPreferences by lazy {
        ZulipApp.appContext.getSharedPreferences(
            KEY_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }

    override fun getOwnUserId(): Int =
        sharedPreferences.getInt(KEY_OWN_USER_ID, DEFAULT_USER_ID)

    override fun setOwnUserId(userId: Int) =
        sharedPreferences.edit().putInt(KEY_OWN_USER_ID, userId).apply()

    companion object {

        private const val KEY_SHARED_PREFS = "ZULIP_APP_SHARED_PREFS"

        private const val KEY_OWN_USER_ID = "KEY_OWN_USER_ID"

         const val DEFAULT_USER_ID = 0

    }
}