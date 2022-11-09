package com.setjy.practiceapp.data.local.storage

import android.content.SharedPreferences
import com.setjy.practiceapp.data.local.db.dao.UserStorage

class UserStorageImpl(private val sharedPreferences: SharedPreferences) : UserStorage {

    override fun getOwnUserId(): Int =
        sharedPreferences.getInt(KEY_OWN_USER_ID, DEFAULT_USER_ID)

    override fun insertOwnUserId(userId: Int) =
        sharedPreferences.edit().putInt(KEY_OWN_USER_ID, userId).apply()

    companion object {

        private const val KEY_OWN_USER_ID = "KEY_OWN_USER_ID"

        const val DEFAULT_USER_ID = 0
    }
}