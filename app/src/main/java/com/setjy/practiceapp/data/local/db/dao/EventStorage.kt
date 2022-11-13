package com.setjy.practiceapp.data.local.db.dao

interface EventStorage {

    fun getLastEventId(): Int

    fun insertLastEventId(lastEventId: Int)
}