package com.setjy.practiceapp.presentation.ui.channels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.ui.people.Search
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ChannelsScreen(
    state: ChannelsState,
    onSearchValueChange: (String) -> Unit,
    search: String,
    onPageSelected: (Int) -> Unit,
    onStreamClick: (StreamItemUI, isSubscribed: Boolean) -> Unit,
    onTopicClick: (topicName: String, streamName: String) -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    val subscribed = Page.SUBSCRIBED.ordinal
    val allStreams = Page.ALL_STREAMS.ordinal

    LaunchedEffect(pagerState.currentPage) {
        onPageSelected(pagerState.currentPage)
    }



    Column(Modifier.fillMaxSize()) {
        Search(
            onValueChange = { onSearchValueChange.invoke(it) },
            search = search,
            placeholderText = stringResource(R.string.ph_search)
        )
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.textPrimary
        ) {
            ChannelsTab(
                onTabClick = {
                    if (pagerState.currentPage != subscribed) scope.launch {
                        pagerState.animateScrollToPage(
                            subscribed
                        )
                    }
                },
                text = stringResource(R.string.tab_subscribed),
                isSelected = pagerState.currentPage == subscribed
            )
            ChannelsTab(
                onTabClick = {
                    if (pagerState.currentPage != Page.ALL_STREAMS.ordinal) scope.launch {
                        pagerState.animateScrollToPage(allStreams)
                    }
                },
                stringResource(R.string.tab_all_streams),
                isSelected = pagerState.currentPage == allStreams
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Top
        ) { pageIndex ->
            StreamListScreen(
                items = if (pageIndex == subscribed) state.visibleItemsSubscribed else state.visibleItems,
                onStreamClick = {
                    onStreamClick.invoke(it, pageIndex == subscribed)
                },
                onTopicClick = { topicName, streamName ->
                    onTopicClick.invoke(
                        topicName,
                        streamName
                    )
                })
        }
    }
}

@Composable
private fun ChannelsTab(
    onTabClick: () -> Unit,
    text: String,
    isSelected: Boolean
) {
    Tab(
        selected = isSelected,
        onClick = { onTabClick.invoke() },
        text = {
            Text(
                text,
                style = AppTheme.typography.textDefault,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun ChannelsScreenPreview() {
    val mockStreams = List(5) { streamId ->
        StreamItemUI(
            streamId = streamId,
            streamName = "Stream #$streamId",
            isExpanded = Random.nextBoolean(),
            listOfTopics = List(3) { topicId ->
                TopicItemUI(
                    topicId = topicId,
                    topicName = "Topic $topicId in Stream $streamId",
                    messageCount = (10..100).random(),
                    parentId = streamId,
                    parentName = "Stream #$streamId",
                    backgroundColor = "#2A9D8F"
                )
            }
        )
    }
    val mockStreams2 = List(5) { streamId ->
        StreamItemUI(
            streamId = streamId,
            streamName = "Stream #$streamId",
            isExpanded = Random.nextBoolean(),
            listOfTopics = List(3) { topicId ->
                TopicItemUI(
                    topicId = topicId,
                    topicName = "Topic $topicId in Stream $streamId",
                    messageCount = (10..100).random(),
                    parentId = streamId,
                    parentName = "Stream #$streamId",
                    backgroundColor = "#2A9D8F"
                )
            }
        )
    }

    val mockState = ChannelsState(
        selectedTab = 0,
        visibleItems = mockStreams.flatMap { stream ->
            if (stream.isExpanded) {
                listOf(stream) + stream.listOfTopics
            } else {
                listOf(stream)
            }
        },
        visibleItemsSubscribed = mockStreams2.flatMap { stream ->
            if (stream.isExpanded) {
                listOf(stream) + stream.listOfTopics
            } else {
                listOf(stream)
            }
        },
        isLoading = false,
        search = "Sample search"
    )

    ZulipTheme {
        ChannelsScreen(
            state = mockState,
            onSearchValueChange = {},
            search = "",
            onStreamClick = { _, _ -> },
            onTopicClick = { _, _ -> },
            onPageSelected = { _ -> }
        )
    }

}
