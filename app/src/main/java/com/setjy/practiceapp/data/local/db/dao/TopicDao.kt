package com.setjy.practiceapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.setjy.practiceapp.data.local.model.TopicEntity

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic WHERE stream_id = :streamId")
    fun getTopicsByStreamId(streamId: Int): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTopics(topics: List<TopicEntity>)
}
