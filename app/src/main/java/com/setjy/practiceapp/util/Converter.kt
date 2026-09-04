package com.setjy.practiceapp.util

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

fun getMessageTimeStamp(timeStamp: Long): String {
    val secondsToMillisMultiplier = 1000L
    val date = Date(timeStamp * secondsToMillisMultiplier)
    val sdf = SimpleDateFormat("HH:mm", Locale("ru"))
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}
