package com.setjy.practiceapp.presentation.ui.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment.Reactions
import kotlin.math.max
import kotlin.random.Random

private object MessageDimens {
    val avatarSize = 38.dp
    val bubbleStartMargin = 10.dp
    val cardCornerRadius = 18.dp
    val msgMaxWidth = 218.dp
    val flxMaxWidth = 266.dp
}

@Composable
fun IncomingMessage(
    modifier: Modifier = Modifier,
    avatar: @Composable () -> Unit,
    fullName: @Composable () -> Unit,
    content: @Composable () -> Unit,
    timestamp: @Composable () -> Unit,
    emojis: @Composable () -> Unit,
) {
    Message(
        modifier = modifier,
        avatar = avatar,
        fullName = fullName,
        content = content,
        timestamp = timestamp,
        emojis = emojis,
        isOutgoing = false
    )
}

@Composable
fun OutgoingMessage(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    timestamp: @Composable () -> Unit,
    emojis: @Composable () -> Unit,
) {
    Message(
        modifier = modifier,
        avatar = {},
        fullName = {},
        content = content,
        timestamp = timestamp,
        emojis = emojis,
        isOutgoing = true
    )
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    avatar: @Composable () -> Unit,
    fullName: @Composable () -> Unit,
    content: @Composable () -> Unit,
    timestamp: @Composable () -> Unit,
    emojis: @Composable () -> Unit,
    isOutgoing: Boolean
) {

    var bubbleInfo: BubbleInfo? = remember() { null }
    val dimens = AppTheme.dimens
    val colors = AppTheme.colors

    Layout(
        contents = listOf(avatar, fullName, content, timestamp, emojis),
        modifier = modifier.drawBehind {
            bubbleInfo?.let { info ->
                drawRoundRect(
                    color = if (isOutgoing) colors.accent else colors.backgroundSecondary,
                    topLeft = Offset(info.offsetX, 0f),
                    size = Size(width = info.width, height = info.height),
                    cornerRadius = CornerRadius(MessageDimens.cardCornerRadius.toPx())
                )
            }
        }
    ) { (avatarMeasurables, fullNameMeasurables, contentMeasurables, timestampMeasurables, emojisMeasurables), constraints ->


        val marginMediumPx = dimens.marginMedium.roundToPx()
        val marginSmallPx = dimens.marginSmall.roundToPx()

        val avatarSize = MessageDimens.avatarSize.roundToPx()
        val avatarConstraints = Constraints.fixed(
            avatarSize, avatarSize
        )
        val avatarPlaceable = avatarMeasurables.firstOrNull()?.measure(avatarConstraints)

        val avatarWidth = avatarPlaceable?.width ?: 0

        val textConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = MessageDimens.msgMaxWidth.roundToPx()
        )

        val fullNamePlaceable = fullNameMeasurables.firstOrNull()?.measure(textConstraints)
        val contentPlaceable = contentMeasurables.firstOrNull()?.measure(textConstraints)
        val timestampPlaceable = timestampMeasurables.firstOrNull()?.measure(constraints)

        val bubbleContentWidth = maxOf(fullNamePlaceable?.width ?: 0, contentPlaceable?.width ?: 0)
        val bubbleWidth = bubbleContentWidth + marginMediumPx * 2
        val bubbleHeight = marginSmallPx * 2 +
                (fullNamePlaceable?.height ?: 0) +
                (contentPlaceable?.height ?: 0) +
                dimens.marginMicro.roundToPx() +
                (timestampPlaceable?.height ?: 0)

        val emojisPlaceable = emojisMeasurables.firstOrNull()
            ?.measure(constraints.copy(maxWidth = MessageDimens.flxMaxWidth.roundToPx()))

        val msgBottomPadding = dimens.marginDefault.roundToPx()

        val totalHeight = max(
            avatarSize,
            bubbleHeight + (emojisPlaceable?.height?.let { it + marginMediumPx + msgBottomPadding }
                ?: 0)
        )

        layout(constraints.maxWidth, totalHeight) {

            val bubbleX = if (isOutgoing) {
                constraints.maxWidth - bubbleWidth
            } else {
                avatarWidth + MessageDimens.bubbleStartMargin.roundToPx()
            }

            val contentY = if (isOutgoing) {
                marginMediumPx
            } else {
                marginSmallPx + (fullNamePlaceable?.height ?: 0)
            }

            val contentX = if (isOutgoing) {
                bubbleX + marginMediumPx
            } else {
                marginMediumPx + avatarWidth + MessageDimens.bubbleStartMargin.roundToPx()
            }

            bubbleInfo = BubbleInfo(
                width = bubbleWidth.toFloat(),
                height = bubbleHeight.toFloat(),
                offsetX = bubbleX.toFloat(),
            )

            avatarPlaceable?.placeRelative(x = 0, y = 0)
            fullNamePlaceable?.placeRelative(
                x = contentX,
                y = marginSmallPx
            )

            contentPlaceable?.placeRelative(
                x = contentX,
                y = contentY
            )

            timestampPlaceable?.placeRelative(
                x = bubbleX + bubbleWidth - marginMediumPx - timestampPlaceable.width,
                y = contentY + (contentPlaceable?.height ?: 0)
            )

            emojisPlaceable?.placeRelative(
                x = if (isOutgoing) constraints.maxWidth - emojisPlaceable.width else bubbleX,
                y = bubbleHeight + marginMediumPx
            )
        }
    }
}

@Immutable
private data class BubbleInfo(
    val width: Float,
    val height: Float,
    val offsetX: Float,
)

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun MessagePreview() {

    val emojis = Reactions.emojiUISet.shuffled().take(8)

    ZulipTheme {
        Column() {
            OutgoingMessage(
                content = {
                    Text(
                        text = "loremloremloremloremloremloremloremloremloremloremloremloremloremloremlorem",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                timestamp = {
                    Text(
                        text = "12:45",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                },
                emojis = {
                    FlexboxLayout(
                        isRtL = true,
                        modifier = Modifier
                            .widthIn(max = MessageDimens.flxMaxWidth)
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
                },
            )

            IncomingMessage(
                avatar = {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                },
                fullName = {
                    Text(
                        text = "Kayden Hartman",
                        color = Color(0xFF4AB54D),
                        fontSize = 14.sp
                    )
                },
                content = {
                    Text(
                        text = "loremloremloremloremloremloremloremloremloremloremloremloremloremloremlorem",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                timestamp = {
                    Text(
                        text = "12:45",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                },
                emojis = {
                    FlexboxLayout(
                        isRtL = false,
                        modifier = Modifier
                            .widthIn(max = MessageDimens.flxMaxWidth)
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
                },
            )
        }
    }
}
