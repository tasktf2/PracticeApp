package com.setjy.practiceapp.presentation.ui.channels.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme

@Composable
fun StreamItem(item: StreamItemUI, onClick: (StreamItemUI) -> Unit = {}) {

    val rotationAngle by animateFloatAsState(
        targetValue = if (item.isExpanded) 180f else 0f, label = "ArrowRotation"
    )

    val arrowTint by animateColorAsState(
        targetValue = if (item.isExpanded) AppTheme.colors.textPrimary else AppTheme.colors.inactive,
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .heightIn(min = AppTheme.dimens.streamHeight)
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(horizontal = AppTheme.dimens.streamGap)
            .clickable { onClick.invoke(item) }) {

        Text(
            text = item.streamName,
            color = AppTheme.colors.textPrimary,
            fontSize = AppTheme.typography.default,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painterResource(R.drawable.ic_round_arrow_prev_24),
            contentDescription = null,
            tint = arrowTint,
            modifier = Modifier.rotate(rotationAngle)
        )
    }
}

@Preview
@Composable
private fun StreamItemPreview() {
    ZulipTheme {
        var item by remember { mutableStateOf(StreamItemUI(streamId = 1, streamName = "TODO()")) }
        StreamItem(
            item = item, onClick = { item = item.copy(isExpanded = !item.isExpanded) }
        )
    }
}

