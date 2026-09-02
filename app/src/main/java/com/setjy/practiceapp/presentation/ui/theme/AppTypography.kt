package com.setjy.practiceapp.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    val colors: AppColors,
    private val micro: TextUnit = 12.sp,
    private val extraSmall: TextUnit = 14.sp,
    private val small: TextUnit = 16.sp,
    private val default: TextUnit = 18.sp,
    private val title: TextUnit = 20.sp,
    private val large: TextUnit = 22.sp,
    private val larger: TextUnit = 24.sp,
    private val header: TextUnit = 32.sp,

    val textMicro: TextStyle = TextStyle(color = colors.textPrimary, fontSize = micro),
    val textExtraSmall: TextStyle = TextStyle(color = colors.textSecondary, fontSize = extraSmall),
    val textSmall: TextStyle = TextStyle(color = colors.textPrimary, fontSize = small),
    val textDefault: TextStyle = TextStyle(color = colors.textPrimary, fontSize = default),
    val textTitle: TextStyle = TextStyle(color = colors.textPrimary, fontSize = title),
    val textLarge: TextStyle = TextStyle(color = colors.textPrimary, fontSize = large),
    val textLarger: TextStyle = TextStyle(color = colors.textPrimary, fontSize = larger),
    val textHeader: TextStyle = TextStyle(color = colors.textPrimary, fontSize = header),
)