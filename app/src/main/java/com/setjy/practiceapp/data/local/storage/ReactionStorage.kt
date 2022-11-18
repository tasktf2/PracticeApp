package com.setjy.practiceapp.data.local.storage

import com.setjy.practiceapp.data.local.db.dao.ReactionDao
import com.setjy.practiceapp.data.local.model.MessageWithReactionsEntity
import com.setjy.practiceapp.data.local.model.ReactionEntity
import javax.inject.Inject

class ReactionStorage @Inject constructor(private val reactionDao: ReactionDao) {

    fun insertReactions(messages: List<MessageWithReactionsEntity>) = messages.forEach { message ->
        reactionDao.insertReactions(
            message.reactions.map { reaction ->
                ReactionEntity(
                    code = reaction.code,
                    name = reaction.name,
                    messageId = message.message.messageId,
                    userId = reaction.userId
                )
            })
    }

    fun insertReaction(reaction: ReactionEntity) = reactionDao.insertReaction(reaction)

    fun deleteReaction(reaction: ReactionEntity) = reactionDao.deleteReaction(reaction)
}
