package com.setjy.practiceapp.presentation.model

import com.setjy.practiceapp.presentation.base.Item
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped

data class EmojiUI(
    val emojiName: String,
    val code: String,
    val isSelected: Boolean = false,
) : ViewTyped, Item {

    val codeString = String(Character.toChars(code.toInt(16)))
}