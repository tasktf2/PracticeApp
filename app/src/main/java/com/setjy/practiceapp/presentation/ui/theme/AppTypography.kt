package com.setjy.practiceapp.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Immutable
data class AppTypography(
    val micro: TextUnit = 14.sp,
    val small: TextUnit = 16.sp,
    val default: TextUnit = 18.sp,
    val large: TextUnit = 22.sp,
    val larger: TextUnit = 24.sp,
    val header: TextUnit = 32.sp,
)