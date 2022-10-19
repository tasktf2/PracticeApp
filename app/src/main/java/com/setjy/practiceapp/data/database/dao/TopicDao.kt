package com.setjy.practiceapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.setjy.practiceapp.data.database.entity.TopicItemUI

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic WHERE parent_id = :streamId")
    fun getTopicsByStreamId(streamId: Int): List<TopicItemUI>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTopics(topics: List<TopicItemUI>)
}
