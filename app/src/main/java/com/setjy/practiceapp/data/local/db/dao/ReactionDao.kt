package com.setjy.practiceapp.data.local.db.dao

import androidx.room.*
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
