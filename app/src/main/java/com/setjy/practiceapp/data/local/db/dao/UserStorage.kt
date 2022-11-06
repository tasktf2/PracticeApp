package com.setjy.practiceapp.data.local.db.dao

interface UserStorage {

    fun getOwnUserId(): Int

    fun insertOwnUserId(userId: Int)
}