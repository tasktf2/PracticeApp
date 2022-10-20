package com.setjy.practiceapp.data.database.dao

import androidx.room.*
import com.setjy.practiceapp.data.network.response.EmojiRemote

@Dao
interface ReactionsDao {
    @Query("SELECT * FROM reaction WHERE message_id=:messageId")
    fun getReactionsByMessageId(messageId: Int): List<EmojiRemote>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReactions(reactions: List<EmojiRemote>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReaction(reaction: EmojiRemote)

    @Delete
    fun deleteReaction(reaction: EmojiRemote)
}
