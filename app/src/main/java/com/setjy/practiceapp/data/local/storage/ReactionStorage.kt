package com.setjy.practiceapp.data.local.storage

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.model.MessageWithReactionsEntity
import com.setjy.practiceapp.data.model.ReactionEntity

class ReactionStorage constructor(context: Context) {
    private val database: ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        context.getString(R.string.database_name)
    ).build()

    private val reactionsDao = database.reactionDao()

    fun insertReactions(messages: List<MessageWithReactionsEntity>) = messages.forEach { message ->
        reactionsDao.insertReactions(
            message.reactions.map { reaction ->
                ReactionEntity(
                    code = reaction.code,
                    name = reaction.name,
                    messageId = message.message.messageId,
                    userId = reaction.userId
                )
            })
    }

    fun insertReaction(reaction: ReactionEntity) = reactionsDao.insertReaction(reaction)

    fun deleteReaction(reaction: ReactionEntity) = reactionsDao.deleteReaction(reaction)

    companion object {
        private var STORAGE_INSTANCE: ReactionStorage? = null
        fun initialize(context: Context) {
            if (STORAGE_INSTANCE == null) STORAGE_INSTANCE = ReactionStorage(context)
        }

        fun get(): ReactionStorage {
            return STORAGE_INSTANCE
                ?: throw IllegalStateException("ReactionsStorage must be initialized")
        }

    }

}
