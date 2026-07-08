package com.setjy.practiceapp.presentation.ui.channels.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.ui.channels.TopicItemUI
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme

@Composable
fun TopicItem(
    item: TopicItemUI,
    onClick: (topicName: String, streamName: String) -> Unit = { _, _ -> }
) {
    val accent = AppTheme.colors.accent
    val bgColor: Color = remember(item.backgroundColor, accent) {
        try {
            item.backgroundColor?.toColorInt()?.let { Color(it) } ?: accent

        } catch (_: Throwable) {
            accent
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = AppTheme.dimens.topicHeight)
            .background(bgColor)
            .clickable { onClick.invoke(item.topicName, item.parentName) }
            .padding(horizontal = AppTheme.dimens.topicGap),

        ) {


        Text(
            text = item.topicName,
            color = AppTheme.colors.textPrimary,
            fontSize = AppTheme.typography.default,
            modifier = Modifier
                .padding(end = AppTheme.dimens.marginBig)
                .weight(1f)
        )

        Text(
            item.messageCount.toString(),
            color = AppTheme.colors.textPrimary,
            fontSize = AppTheme.typography.small,
            modifier = Modifier.padding(end = AppTheme.dimens.marginMicro)
        )

        Text(
            stringResource(R.string.topic_message),
            color = AppTheme.colors.textPrimary,
            fontSize = AppTheme.typography.small
        )

    }
}

@Preview
@Composable
private fun TopicItemPreview() {
    ZulipTheme {
        TopicItem(
            item = TopicItemUI(
                topicId = 1,
                topicName = "TestingTestingTestingTestingTestingTestingTestingTestingTestingTesting",
                messageCount = 100,
                parentId = 1,
                parentName = "",
                backgroundColor = "2A9D8F"
            )
        )
    }
}