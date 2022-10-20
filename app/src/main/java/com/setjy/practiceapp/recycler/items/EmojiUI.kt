package com.setjy.practiceapp.recycler.items

import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.ViewTyped

data class EmojiUI(
    val emojiName: String,
    val code: String,
    val isSelected: Boolean = false,
    override val viewType: Int = R.layout.item_emoji
) : ViewTyped {

    val codeString = String(Character.toChars(code.toInt(16)))
}