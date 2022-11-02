package com.setjy.practiceapp.data.local.pref

interface Preferences {

    fun getOwnUserId(): Int

    fun setOwnUserId(userId:Int)
}