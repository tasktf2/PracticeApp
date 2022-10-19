package com.setjy.practiceapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.setjy.practiceapp.data.database.entity.StreamItemDB

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream WHERE is_subscribed BETWEEN :isSubscribed AND 1 ")
    fun getStreams(isSubscribed: Boolean): List<StreamItemDB>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllStreams(streams: List<StreamItemDB>)
}
