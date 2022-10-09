package com.setjy.practiceapp.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.Px
import com.setjy.practiceapp.data.Data
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*
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

fun getTimeStamp(timeStamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM", Locale("ru"))
    val date = Date(timeStamp)
    val dfs = DateFormatSymbols()
    val shortMonths = listOf(
        "Янв",
        "Фев",
        "Мар",
        "Апр",
        "Мая",
        "Июн",
        "Июл",
        "Авг",
        "Сен",
        "Окт",
        "Ноя",
        "Дек"
    )
    dfs.shortMonths = shortMonths.toTypedArray()
    sdf.dateFormatSymbols = dfs
    return sdf.format(date)
}

fun getMessageTimeStampSeconds(timeStamp: Long): String {
    val secondsToMillisMultiplier = 1000L
    val date = Date(timeStamp * secondsToMillisMultiplier)
    val sdf = SimpleDateFormat("HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun getMessageTimeStampMillis(timeStamp: Long): String {
    val date = Date(timeStamp)
    val sdf = SimpleDateFormat("HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}
