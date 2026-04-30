package com.setjy.practiceapp.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LocalAppColors = staticCompositionLocalOf<AppColors> { error("No colors provided yet") }
private val LocalAppDimens = staticCompositionLocalOf<AppDimens> { error("No dimens provided yet") }
private val LocalTypography =
    staticCompositionLocalOf<AppTypography> { error("No typography provided yet") }

@Composable
fun ZulipTheme(
    content: @Composable () -> Unit
) {
    val colors = DarkPalette
    val dimens = AppDimens()
    val typography = AppTypography()


    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppDimens provides dimens,
        LocalTypography provides typography
    ) {
        MaterialTheme(
            content = {
                Surface(content = content, color = colors.background)
            }
        )

    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val dimens: AppDimens
        @Composable
        get() = LocalAppDimens.current
    
    val typography: AppTypography
        @Composable
        get() = LocalTypography.current
}

private val DarkPalette = AppColors(
    textPrimary = Color(0xFFFAFAFA),
    textSecondary = Color(0xFFA1A1A1),
    background = Color(0xFF121212),
    online = Color(0xFF4AB54D),
    surface = Color(0xFF1E1E1E),
    disabled = Color(0xFF666666),
    idle = Color(0xFFFF4500),
    backgroundSecondary = Color(0xFF1C1C1C),
    accent = Color(0xFF2A9D8F),
    hint = Color(0xFFCCCCCC),
)
