package com.setjy.practiceapp.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.setjy.practiceapp.data.local.model.ReactionEntity

@Dao
interface ReactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReactions(reactions: List<ReactionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReaction(reaction: ReactionEntity)

    @Delete
    fun deleteReaction(reaction: ReactionEntity)
}
