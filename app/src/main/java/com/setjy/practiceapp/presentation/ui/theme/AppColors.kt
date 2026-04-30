package com.setjy.practiceapp.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Текстовые цвета
//val ColorTextWhite = Color(0xFFFAFAFA)
//val ColorTextSecondary = Color(0xFFA1A1A1)
//val ColorEmojiCounterWhite = Color(0xFFEAEAEA)
//val ColorHintGrey = Color(0xFFCCCCCC)
//val ColorTimeDividerTextGrey = Color(0xFF999999)
//val ColorBottomNavNotSelectedGrey = Color(0xFF919191)

// Серые и фоновые элементы
//val ColorIndicatorGrey = Color(0xFF5C5C5C)
//val ColorItemBottomFragmentGrey = Color(0xFF555555)
//val ColorInactiveGrey = Color(0xFF666666)
//val ColorEmojiBgSelected = Color(0xFF3A3A3A)

// Черные фоны
//val ColorBgEditText = Color(0xFF282828)
//val ColorBgBottomNavViewBlack = Color(0xFF1E1E1E)
//val ColorBgBlackSecondary = Color(0xFF1C1C1C)
//val ColorBackgroundBlack = Color(0xFF121212)
//val ColorBgTimeDividerBlack = Color(0xFF070707)

// Акцентные цвета
//val ColorAppCyan = Color(0xFF2A9D8F)
//val ColorBgSendGradientEnd = Color(0xFF006153)
//val ColorOnlineGreen = Color(0xFF4AB54D)
//val ColorIdleOrange = Color(0xFFFF4500)

@Immutable
data class AppColors(
    val textPrimary: Color,
    val textSecondary: Color,

    val online: Color,
    val disabled: Color,
    val idle: Color,
    val hint: Color,
    val background: Color,
    val backgroundSecondary: Color,
    val surface: Color,
    val accent: Color,
)
