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
import kotlin.math.max
import kotlin.random.Random

private object FlexboxDimens {
    val horizontalSpacing = 6.dp
    val verticalSpacing = 10.dp
}

@Composable
fun FlexboxLayout(
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

        var left = 0
        var top = 0
        val pHeight = (placeables.firstOrNull()?.height ?: 0) + vSpacing

        placeables.forEach {
            if (it.width + left > childConstraints.maxWidth) {
                left = 0
                top += pHeight
            }
            left += it.width + hSpacing
        }
        top += pHeight
        top = max(top, pHeight)

        layout(childConstraints.maxWidth, top) {
            left = 0
            top = 0
            placeables.forEach {
                if (it.width + left > childConstraints.maxWidth) {
                    left = 0
                    top += pHeight
                }
                it.placeRelative(left, top)
                left += it.width + hSpacing

            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun FlexboxLayoutPreview() {
    val emojis = Reactions.emojiUISet.shuffled().take(8)

    ZulipTheme {
        FlexboxLayout(
            modifier = Modifier
                .padding(16.dp)
        ) {

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