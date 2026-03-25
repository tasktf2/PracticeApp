package com.setjy.practiceapp.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireLayoutCoordinates
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.toSize

private class ShimmerNode(
    var progress: State<Float>,
    var primaryColor: Color,
    var secondaryColor: Color,
    var show: Boolean
) : Modifier.Node(), DrawModifierNode {
    override fun ContentDrawScope.draw() {
        if (show)
            drawRect(
                brush = getShimmerBrush(
                    progress = progress,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor
                )
            ) else drawContent()
    }

    private fun getShimmerBrush(
        progress: State<Float>,
        primaryColor: Color,
        secondaryColor: Color
    ): Brush {
        val coordinates: LayoutCoordinates = requireLayoutCoordinates()
        val positionInRoot = coordinates.positionInRoot()

        val rootSize: Size = coordinates.findRoot().size.toSize()
        val totalDistance = rootSize.width + rootSize.height

        val globalOffset = totalDistance * progress.value

        val localOffset = globalOffset - (positionInRoot.x + positionInRoot.y)

        val shimmerWidth = 400f

        return Brush.linearGradient(
            colors = listOf(primaryColor, secondaryColor, primaryColor),
            start = Offset(x = localOffset - shimmerWidth, y = localOffset - shimmerWidth),
            end = Offset(x = localOffset, y = localOffset)
        )
    }

    private fun LayoutCoordinates.findRoot(): LayoutCoordinates {
        var root = this
        while (root.parentLayoutCoordinates != null) {
            root = root.parentLayoutCoordinates!!
        }
        return root
    }


}

private data class ShimmerElement(
    private val progress: State<Float>,
    private val primaryColor: Color,
    private val secondaryColor: Color,
    private val show: Boolean
) : ModifierNodeElement<ShimmerNode>() {

    override fun create(): ShimmerNode = ShimmerNode(
        progress = progress,
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        show = show
    )

    override fun update(node: ShimmerNode) {
        node.apply {
            progress = progress
            primaryColor = primaryColor
            secondaryColor = secondaryColor
            show = show
            invalidateDraw()
        }

    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shimmer"
        properties["show"] = show
    }
}

fun Modifier.shimmer(
    progress: State<Float>,
    show: Boolean,
    primaryColor: Color = Color.White,
    secondaryColor: Color = Color.DarkGray
): Modifier {
    return if (show) {
        this.then(
            ShimmerElement(
                progress = progress,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                show = true
            )
        )
    } else this
}

@Composable
fun rememberShimmerProgress(durationMillis: Int = 1500): State<Float> {
    val transition = rememberInfiniteTransition(label = "GlobalShimmer")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            )
        ),
        label = "ShimmerPhase"
    )
}
