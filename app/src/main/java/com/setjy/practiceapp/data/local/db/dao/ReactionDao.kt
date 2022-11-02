package com.setjy.practiceapp.data.local.db.dao

import androidx.room.*
import com.setjy.practiceapp.data.model.ReactionEntity

@Dao
interface ReactionDao {
    @Query("SELECT * FROM reaction WHERE message_id=:messageId")
    fun getReactionsByMessageId(messageId: Int): List<ReactionEntity> //todo delete?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReactions(reactions: List<ReactionEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReaction(reaction: ReactionEntity)

    @Delete
    fun deleteReaction(reaction: ReactionEntity)
}
