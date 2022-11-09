package com.setjy.practiceapp.presentation.view

import android.content.Context
import android.util.AttributeSet
import com.setjy.practiceapp.presentation.model.EmojiUI

class EmojiFlexboxLayoutRight @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FlexboxLayoutRight(context, attrs, defStyleAttr, defStyleRes) {

    var onEmojiClick: ((emojiName: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: (() -> Unit)? = null

    fun setEmojis(items: List<EmojiUI>) {
        removeAllViews()
        val codeToNumber = items.groupBy { it.code }.mapValues { it.value.count() }
        items.distinctBy { it.code }.forEach { emoji ->
            val emojiView = EmojiView(context).apply {
                emojiUnicode = emoji.codeString
                emojiCounter = codeToNumber[emoji.code] ?: 0
                flagIsSelected = items.any { it.code == emoji.code && it.isSelected }
                setOnClickListener { onEmojiClick?.invoke(emoji.emojiName, emoji.code) }
            }
            addView(emojiView)
        }
        if (items.isNotEmpty()) {
            addView(EmojiView(context).apply {
                emojiUnicode = "+"
                setOnClickListener { onAddEmojiClick?.invoke() }
            })
        }
    }
}