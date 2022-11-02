package com.setjy.practiceapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.setjy.practiceapp.data.base.ModelRemote
import com.setjy.practiceapp.data.model.ReactionEntity
import com.setjy.practiceapp.domain.model.ReactionDomain

data class ReactionsResponse(
    @SerializedName("emoji_code") val emojiCode: String,
    @SerializedName("emoji_name") val emojiName: String,
    @SerializedName("user_id") val userId: Int
) : ModelRemote() {
    fun toDomain(messageId: Int) = ReactionDomain( //todo refactor to interface?
        code = emojiCode, name = emojiName, userId = userId, messageId = messageId
    )

    fun toEntity(messageId: Int) = ReactionEntity(
        code = emojiCode, name = emojiName, userId = userId, messageId = messageId
    )
}

data class EmojiToggleResponse(
    @SerializedName("msg") val message: String,
    @SerializedName("code") val code: String?,
    @SerializedName("result") val result: String
)