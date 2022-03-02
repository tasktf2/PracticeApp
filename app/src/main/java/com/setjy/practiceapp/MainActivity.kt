package com.setjy.practiceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.setjy.practiceapp.util.getEmojiByUnicode
import com.setjy.practiceapp.view.EmojiView
import com.setjy.practiceapp.view.FlexboxLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flexboxLayout = findViewById<FlexboxLayout>(R.id.flexbox)
        val emojiData = mutableListOf(
            EmojiData("U+1F614", 0),
            EmojiData("0x1F61A", 1),
            EmojiData("0x1F61C", 2),
            EmojiData("0x1F62A", 3),
            EmojiData("0x1F630", 4),
            EmojiData("0x1F633", 5),
            EmojiData("0x1F649", 6),
            EmojiData("0x1F64B", 7),
            EmojiData("0x1F64F", 8),
            EmojiData("U+1F614", 9),
            EmojiData("0x1F61A", 10),
            EmojiData("0x1F61C", 11),
            EmojiData("0x1F62A", 12),
            EmojiData("0x1F630", 13),
            EmojiData("0x1F633", 14),
            EmojiData("+")
        )
        emojiData.forEach { child ->
            EmojiView(this).apply {
                this.emojiUnicode = getEmojiByUnicode(child.emoji)
                this.emojiCounter = child.counter
                flexboxLayout.addView(this)
                setOnClickListener { }
            }
        }
    }
}


