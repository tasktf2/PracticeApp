package com.setjy.practiceapp.view

import android.content.Context
import android.util.AttributeSet
import com.setjy.practiceapp.recycler.reactions.EmojiNCS

class EmojiFlexboxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr, defStyleRes) {

    var onEmojiClick: ((emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: (() -> Unit)? = null

    //todo refactor this!!
    fun setEmojis(items: List<EmojiNCS>) {
        removeAllViews()
        val codeToNumber = items.groupBy { it.code }.mapValues { it.value.count() }
        items.distinctBy { it.code }.forEach { emoji ->
            val emojiView = EmojiView(context).apply {
                emojiCounter = codeToNumber[emoji.code] ?: 0
                emojiUnicode = emoji.getCodeString()
                flagIsSelected = items.groupBy { it.code }[emoji.code]?.any { it.isSelected } ?: false
                setOnClickListener { onEmojiClick?.invoke(emoji.code) }
            }
            addView(emojiView)
        }
        if (items.isNotEmpty()) addView(
            EmojiView(context).apply {
                emojiUnicode = "+"
                setOnClickListener { onAddEmojiClick?.invoke() }
            }
        )
    }


}
