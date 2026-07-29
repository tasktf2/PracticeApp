package com.setjy.practiceapp.presentation.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimens(
    val marginMicro: Dp = 4.dp,
    val marginSmall: Dp = 6.dp,
    val marginMedium: Dp = 12.dp,
    val marginDefault: Dp = 16.dp,
    val marginBig: Dp = 28.dp,

    val shapeMicro: Dp = 4.dp,

    val avatarSmall: Dp = 64.dp,
    val avatarBig: Dp = 186.dp,

    val status: Dp = 16.dp,
    val statusBorder: Dp = 2.dp,

    val search: Dp = 56.dp,
    val searchGap: Dp = 56.dp,

    val topicGap: Dp = 62.dp,
    val topicHeight: Dp = 42.dp,

    val streamHeight: Dp = 64.dp,
    val streamGap: Dp = 46.dp
)