package com.setjy.practiceapp.data.local.db.dao

import androidx.room.*
import com.setjy.practiceapp.data.local.model.StreamEntity
import com.setjy.practiceapp.data.local.model.StreamWithTopicsEntity

@Dao
interface StreamDao {

    @Transaction
    @Query("SELECT * FROM stream WHERE is_subscribed BETWEEN :isSubscribed AND 1 ")
    fun getStreams(isSubscribed: Boolean): List<StreamWithTopicsEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllStreams(streams: List<StreamEntity>)
}
