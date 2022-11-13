package com.setjy.practiceapp.data.local.storage

import android.content.SharedPreferences
import com.setjy.practiceapp.data.local.db.dao.EventStorage

class EventStorageImpl(private val sharedPreferences: SharedPreferences) : EventStorage {

    override fun getLastEventId(): Int =
        sharedPreferences.getInt(KEY_LAST_EVENT_ID, DEFAULT_LAST_EVENT_ID)

    override fun insertLastEventId(lastEventId: Int) =
        sharedPreferences.edit().putInt(KEY_LAST_EVENT_ID, lastEventId).apply()

    companion object {

        private const val KEY_LAST_EVENT_ID = "KEY_LAST_EVENT_ID"

        const val DEFAULT_LAST_EVENT_ID = -1
    }
}