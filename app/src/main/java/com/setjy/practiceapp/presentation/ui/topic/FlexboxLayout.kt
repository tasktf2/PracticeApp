package com.setjy.practiceapp.presentation.ui.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment.Reactions
import kotlin.random.Random

private object FlexboxDimens {
    val horizontalSpacing = 6.dp
    val verticalSpacing = 10.dp
}

@Composable
fun FlexboxLayout(
    isRtL: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->

        val hSpacing = FlexboxDimens.horizontalSpacing.roundToPx()
        val vSpacing = FlexboxDimens.verticalSpacing.roundToPx()

        val childConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val placeables = measurables.map { it.measure(childConstraints) }

        var x = 0
        var y = 0
        var totalHeight = 0
        val pHeight = (placeables.firstOrNull()?.height ?: 0) + vSpacing

        if (placeables.isNotEmpty()) {
            totalHeight += pHeight
            placeables.forEach {
                if (it.width + x > childConstraints.maxWidth) {
                    x = 0
                    y += pHeight
                    totalHeight += pHeight
                }
                x += it.width + hSpacing
            }
            totalHeight -= vSpacing
        }

        layout(constraints.maxWidth, totalHeight) {
            x = if (isRtL) constraints.maxWidth else 0
            y = 0

            placeables.forEach {
                when {
                    isRtL -> {
                        if (x - it.width < 0) {
                            x = constraints.maxWidth
                            y += pHeight
                        }
                        it.placeRelative(x - it.width, y)
                        x = x - it.width - hSpacing

                    }
                    else -> {
                        if (it.width + x > childConstraints.maxWidth) {
                            x = 0
                            y += pHeight
                        }
                        it.placeRelative(x, y)
                        x += it.width + hSpacing
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun FlexboxLayoutPreview() {
    val emojis = Reactions.emojiUISet.shuffled().take(6)

    ZulipTheme {
        FlexboxLayout(isRtL = true) {

            emojis.forEach { emoji ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFF2B2B2B), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${emoji.codeString} ${Random.nextInt(100)}",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}