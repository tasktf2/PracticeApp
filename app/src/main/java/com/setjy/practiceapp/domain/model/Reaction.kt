package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.data.local.pref.AppPreferences
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.model.EmojiUI

data class ReactionDomain(
    val code: String,
    val name: String,
    val userId: Int,
    val messageId: Int
) : Model()

class ReactionMapper : DomainMapper<EmojiUI, ReactionDomain> {
    override fun mapToPresentation(model: ReactionDomain): EmojiUI = EmojiUI(
        emojiName = model.name,
        code = model.code,
        isSelected = model.userId == AppPreferences().getOwnUserId()
    )
}