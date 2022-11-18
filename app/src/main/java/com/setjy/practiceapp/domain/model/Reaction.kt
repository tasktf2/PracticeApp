package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.di.module.topic.TopicModule
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.model.EmojiUI
import javax.inject.Inject
import javax.inject.Named

data class ReactionDomain(
    val code: String,
    val name: String,
    val userId: Int,
    val messageId: Int
) : Model

class ReactionMapper @Inject constructor(@Named(TopicModule.NAMED_USER_ID) private val ownUserId: Int) :
    DomainMapper<ReactionDomain, EmojiUI> {
    override fun mapToPresentation(model: ReactionDomain): EmojiUI = EmojiUI(
        emojiName = model.name,
        code = model.code,
        isSelected = model.userId == ownUserId
    )
}