package com.setjy.practiceapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.setjy.practiceapp.data.model.StreamEntity
import com.setjy.practiceapp.data.model.StreamWithTopicsEntity

@Dao
interface StreamDao {

    @Transaction
    @Query("SELECT * FROM stream WHERE is_subscribed BETWEEN :isSubscribed AND 1 ")
    fun getStreams(isSubscribed: Boolean): List<StreamWithTopicsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllStreams(streams: List<StreamEntity>)
}
