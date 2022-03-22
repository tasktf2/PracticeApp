package com.setjy.practiceapp.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.Px
import kotlin.math.roundToInt

@Px
fun Context.spToPx(sp: Float): Int = spToPx(sp, resources)

@Px
fun Context.dpToPx(dp: Float): Int = dpToPx(dp, resources)

@Px
fun spToPx(sp: Float, resources: Resources): Int {
    return (sp * resources.displayMetrics.scaledDensity).roundToInt()
}

@Px
fun dpToPx(dp: Float, resources: Resources): Int {
    return (dp * resources.displayMetrics.density).roundToInt()
}

fun getEmojiByUnicode(unicode: String): String {
    if (unicode == "+") return unicode
    val code = unicode.toInt(16)
    return String(Character.toChars(code))
}