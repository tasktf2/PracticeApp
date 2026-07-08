package com.setjy.practiceapp.presentation.ui.channels

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.setjy.practiceapp.presentation.base.recycler.base.ViewTyped
import com.setjy.practiceapp.presentation.ui.channels.items.StreamItem
import com.setjy.practiceapp.presentation.ui.channels.items.TopicItem

@Composable
fun StreamListScreen(
    items: List<ViewTyped>?,
    modifier: Modifier = Modifier,
    onStreamClick: (StreamItemUI) -> Unit,
    onTopicClick: (String, String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items.orEmpty()) { viewTyped ->
            when (viewTyped) {
                is StreamItemUI -> StreamItem(item = viewTyped) { onStreamClick.invoke(it) }
                is TopicItemUI -> TopicItem(item = viewTyped) { topicName, parentName ->
                    onTopicClick.invoke(
                        topicName,
                        parentName
                    )
                }
            }
        }

    }
}