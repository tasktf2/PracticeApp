package com.setjy.practiceapp.presentation.ui.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.base.mvi.MviViewModel
import com.setjy.practiceapp.presentation.model.MessageUI
import com.setjy.practiceapp.presentation.ui.theme.AppTheme
import com.setjy.practiceapp.presentation.ui.theme.ZulipTheme
import com.setjy.practiceapp.presentation.ui.topic.TopicAction.AddReaction
import com.setjy.practiceapp.presentation.ui.topic.TopicAction.GetEvents
import com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment.BottomSheetFragment
import com.setjy.practiceapp.presentation.ui.topic.bottom_sheet_fragment.Reactions
import kotlin.random.Random

@Composable
fun TopicScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel<TopicAction, TopicState, TopicEffect>?,
    streamName: String,
    topicName: String,
    onBackClick: () -> Unit,
) {

    val state by viewModel!!.state.subscribeAsState(TopicState())
    val searchValue: String = state.search
    val msgValue: String = state.message.orEmpty()
    val messages = state.messages.orEmpty()


    val isSearchActive = state.isSearchVisible
    val lazyListState = rememberLazyListState()
    val searchIterator: SearchMessagesIterator = remember { SearchMessagesIterator() }
    val isAtTop: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != null && lastVisibleItem.index >= lazyListState.layoutInfo.totalItemsCount - 3 && !state.isPaginationLoading && !state.isPaginationLastPage
        }
    }

    val actions = remember(viewModel, state.isSearchVisible, onBackClick, streamName, topicName) {
        TopicActions(
            onEmojiClick = { messageId: Int, emojiName: String, emojiCode: String ->
                viewModel?.accept(
                    TopicAction.EmojiClicked(
                        messageId = messageId,
                        emojiName = emojiName,
                        emojiCode = emojiCode
                    )
                )
            },
            onLongClick = { viewModel?.accept(TopicAction.ShowBottomSheetFragment(it)) },
            onSendMessage = {
                viewModel?.accept(
                    TopicAction.SendMessage(
                        streamName,
                        topicName
                    )
                )

            },
            onBackClick = {
                //back pressing to hide search group
                if (state.isSearchVisible) {
                    viewModel?.accept(TopicAction.DeleteSearch)
                    viewModel?.accept(TopicAction.AcceptSearchAction(SearchAction.CANCEL))
                } else {
                    onBackClick.invoke()
                }
            },
            onSearchChanged = {
                viewModel?.accept(TopicAction.SearchChanged(it))
            },
            onDeleteClick = {
                searchIterator.reset()
                viewModel?.accept(TopicAction.DeleteSearch)
            },
            onSearchNext = {
                viewModel?.accept(
                    TopicAction.AcceptSearchAction(
                        SearchAction.NEXT
                    )
                )
            },
            onSearchPrev = {
                viewModel?.accept(
                    TopicAction.AcceptSearchAction(
                        SearchAction.PREV
                    )
                )
            },
            onSearchStart = {
                viewModel?.accept(
                    TopicAction.AcceptSearchAction(SearchAction.START)
                )
            },
            onType = { viewModel?.accept(TopicAction.TypeMessage(it)) }

        )
    }

    LaunchedEffect(isAtTop) {
        if (isAtTop) {
            viewModel?.accept(
                TopicAction.StartPagination(
                    streamName,
                    topicName,
                    messages.lastOrNull()?.messageId ?: 0
                )
            )
        }
    }

    LaunchedEffect(state.foundIndices) {
        searchIterator.setMatches(state.foundIndices, state.search)
    }

    LaunchedEffect(searchIterator.currentMatchIndex) {
        if (searchIterator.currentMatchIndex != null && searchIterator.currentMatchIndex!! >= 0) {
            lazyListState.animateScrollToItem(searchIterator.currentMatchIndex!!)

        }
    }

    DisposableEffect(viewModel?.effects) {
        val disposable = viewModel?.effects?.subscribe { effect ->
            when (effect) {
                is TopicEffect.NextSearchAction -> when (effect.action) {

                    SearchAction.START -> if (searchIterator.foundIndices.value.isNotEmpty()) {
                        searchIterator.currentMessage()
                    }

                    SearchAction.NEXT -> if (searchIterator.hasNext()) {
                        searchIterator.nextMessage()
                    }

                    SearchAction.PREV -> if (searchIterator.hasPrevious()) {
                        searchIterator.previousMessage()
                    }

                    SearchAction.CANCEL -> searchIterator.reset()
                }

                is TopicEffect.GetEvents -> viewModel.accept(
                    GetEvents(
                        streamName = streamName,
                        topicName = topicName,
                        queueId = effect.queueId,
                        lastEventId = effect.lastEventId
                    )
                )

                else -> {}
            }
        }
        onDispose { disposable?.dispose() }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background,
        topBar = {
            Column(modifier.fillMaxWidth()) {
                Toolbar(
                    onBackClick = actions.onBackClick,
                    streamName = streamName,
                    searchValue = searchValue,
                    onSearchChanged = actions.onSearchChanged,
                    onDeleteClick = actions.onDeleteClick,
                    isSearchActive = isSearchActive,
                    onSearchClick = actions.onSearchStart
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.backgroundSecondary)
                        .padding(AppTheme.dimens.marginMicro)
                ) {
                    Text(
                        text = "Topic: #$topicName",
                        style = AppTheme.typography.textTitle,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }

        }, content = { paddingValues ->
            LazyColumn(
                state = lazyListState,
                reverseLayout = true,
                contentPadding = PaddingValues(vertical = AppTheme.dimens.marginMedium),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimens.marginDefault)

            ) {
                itemsIndexed(messages) { index, message ->

                    val isHighlighted =
                        remember(
                            index,
                            searchIterator.currentMatchIndex
                        ) { index == searchIterator.currentMatchIndex }
                    if (message.isOutgoingMessage) {
                        OutgoingMessage(
                            message = message,
                            onClick = actions.onEmojiClick,
                            onLongClick = actions.onLongClick,
                            isHighlighted = isHighlighted
                        )
                    } else {
                        IncomingMessage(
                            message = message,
                            onClick = actions.onEmojiClick,
                            onLongClick = actions.onLongClick,
                            isHighlighted = isHighlighted
                        )
                    }
                }
            }

        },
        bottomBar = {
            if (isSearchActive) {
                Row(
                    modifier = Modifier
                        .background(AppTheme.colors.accent)
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = AppTheme.dimens.marginDefault),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${searchIterator.currentMatch}/${searchIterator.foundIndices.value.size}",
                        style = AppTheme.typography.textSmall
                    )
                    Arrow(
                        contentDescription = stringResource(R.string.cd_next),
                        onArrowClick = actions.onSearchNext,
                        isEnabled = searchIterator.hasNext()
                    )
                    Arrow(
                        modifier.rotate(180f),
                        contentDescription = stringResource(R.string.cd_prev),
                        onArrowClick = actions.onSearchPrev,
                        isEnabled = searchIterator.hasPrevious()
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppTheme.dimens.marginExtraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = msgValue,
                        onValueChange = actions.onType,
                        shape = RoundedCornerShape(AppTheme.dimens.shapeInfinite),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.hint_type),
                                color = AppTheme.colors.textPrimary,
                            )
                        },
                        colors = TextFieldDefaults.colors()
                            .copy(
                                unfocusedContainerColor = AppTheme.colors.backgroundSecondary,
                                focusedContainerColor = AppTheme.colors.backgroundSecondary,
                                focusedTextColor = AppTheme.colors.textPrimary,
                                unfocusedTextColor = AppTheme.colors.textPrimary,
                                unfocusedIndicatorColor = AppTheme.colors.backgroundSecondary,
                                focusedIndicatorColor = AppTheme.colors.backgroundSecondary
                            ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = AppTheme.dimens.marginExtraSmall)
                    )
                    Icon(
                        painter = painterResource(R.drawable.btn_send),
                        tint = AppTheme.colors.accent,
                        contentDescription = stringResource(R.string.cd_send_icon),
                        modifier = Modifier
                            .clickable(onClick = actions.onSendMessage)
                            .padding(horizontal = AppTheme.dimens.marginExtraSmall)
                            .size(40.dp)
                    )
                }
            }
        }
    )
}

@Composable
private fun Arrow(
    modifier: Modifier = Modifier,
    contentDescription: String,
    isEnabled: Boolean,
    onArrowClick: () -> Unit
) {
    Icon(
        painter = painterResource(R.drawable.ic_round_arrow_next_24),
        contentDescription = contentDescription,
        modifier = modifier.clickable(enabled = isEnabled) { onArrowClick.invoke() },
        tint = if (isEnabled) AppTheme.colors.textPrimary else AppTheme.colors.textSecondary
    )
}

@Composable
private fun Toolbar(
    onBackClick: () -> Unit,
    streamName: String,
    searchValue: String,
    onSearchChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    isSearchActive: Boolean,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(color = AppTheme.colors.accent)
            .fillMaxWidth()
            .height(50.dp)
            .padding(
                start = AppTheme.dimens.marginDefault,
                top = if (isSearchActive) 0.dp else AppTheme.dimens.marginMedium,
                bottom = if (isSearchActive) 0.dp else AppTheme.dimens.marginMedium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_round_arrow_back_24),
            contentDescription = stringResource(R.string.cd_back),
            tint = AppTheme.colors.textPrimary,
            modifier = Modifier.clickable { onBackClick.invoke() }
        )
        if (isSearchActive) {

            TextField(
                value = searchValue,
                onValueChange = { onSearchChanged.invoke(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppTheme.dimens.marginMedium),
                singleLine = true,

                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = AppTheme.colors.accent,
                    focusedContainerColor = AppTheme.colors.accent,
                    focusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedTextColor = AppTheme.colors.textPrimary,
                    unfocusedIndicatorColor = AppTheme.colors.accent,
                    focusedIndicatorColor = AppTheme.colors.accent
                ),
                trailingIcon = {
                    if (searchValue.isNotEmpty()) {

                        Icon(
                            painterResource(R.drawable.ic_round_delete_24),
                            contentDescription = stringResource(R.string.cd_delete),
                            tint = AppTheme.colors.textPrimary,
                            modifier = Modifier.clickable { onDeleteClick.invoke() }
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.ph_search),
                        style = AppTheme.typography.textDefault,
                        color = AppTheme.colors.hint,
                    )
                }

            )
        } else {
            Text(
                text = streamName,
                style = AppTheme.typography.textTitle,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        horizontal = AppTheme.dimens.marginMedium,
                    )

            )
            Icon(
                painter = painterResource(R.drawable.ic_vector_search_24),
                contentDescription = stringResource(R.string.cd_search_icon),
                tint = AppTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(end = AppTheme.dimens.marginDefault)
                    .clickable { onSearchClick.invoke() }
            )
        }
    }
}

@Composable
private fun IncomingMessage(
    message: MessageUI, onClick: (Int, String, String) -> Unit,
    onLongClick: (Int) -> Unit,
    isHighlighted: Boolean
) {
    IncomingMessage(
        isHighlighted = isHighlighted,
        avatar = {
            AsyncImage(
                model = message.avatarUrl,
                contentDescription = message.username.orEmpty()
            )
        },
        fullName = {
            Text(
                text = message.username.orEmpty(),
                style = AppTheme.typography.textExtraSmall.copy(color = AppTheme.colors.accent)
            )
        },
        content = { Message(message.message) },
        timestamp = { Timestamp(message.timestamp) },
        emojis = { FlexBox(message, isOutgoing = false, onClick = onClick) },
        modifier = Modifier.combinedClickable(
            onLongClick = { onLongClick.invoke(message.messageId) },
            onClick = {}
        )
    )
}

@Composable
private fun OutgoingMessage(
    message: MessageUI,
    onClick: (Int, String, String) -> Unit,
    onLongClick: (Int) -> Unit,
    isHighlighted: Boolean
) {
    OutgoingMessage(
        content = { Message(message.message) },
        timestamp = { Timestamp(message.timestamp) },
        emojis = { FlexBox(message, isOutgoing = true, onClick = onClick) },
        modifier = Modifier.combinedClickable(
            onLongClick = { onLongClick.invoke(message.messageId) },
            onClick = {}
        ),
        isHighlighted = isHighlighted
    )
}

@Composable
private fun Timestamp(timestamp: String) {
    Text(
        text = timestamp,
        style = AppTheme.typography.textMicro
    )
}

@Composable
private fun Message(message: String) {
    Text(
        text = message,
        style = AppTheme.typography.textSmall
    )
}

@Composable
private fun FlexBox(
    message: MessageUI,
    isOutgoing: Boolean,
    onClick: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    FlexboxLayout(isRtL = isOutgoing) {

        val codeToNumber: Map<String, Int> =
            remember {
                message
                    .reactions
                    .groupBy { it.code }
                    .mapValues { it.value.count() }
            }

        message.reactions.forEach { emoji ->

            Text(
                text = "${emoji.codeString} ${codeToNumber[emoji.code]}",
                style = AppTheme.typography.textExtraSmall,
                modifier = Modifier
                    .background(
                        color = if (emoji.isSelected) AppTheme.colors.selected else
                            AppTheme.colors.backgroundSecondary,
                        shape = RoundedCornerShape(AppTheme.dimens.marginSmall)
                    )
                    .clickable { onClick.invoke(message.messageId, emoji.emojiName, emoji.code) }
                    .padding(
                        horizontal = AppTheme.dimens.marginMedium,
                        vertical = AppTheme.dimens.marginExtraSmall
                    )

            )
        }
    }

}

@Preview
@Composable
private fun TopicScreenPreview() {

    val mockMessages = listOf(
        MessageUI(
            userId = 1,
            messageId = 1,
            avatarUrl = null,
            username = "Kayden Hartman",
            message = "Привет! Как продвигается работа над флексбоксом?",
            timestamp = "12:00",
            streamName = "Android",
            topicName = "Compose Layouts",
            isOutgoingMessage = false,
            reactions = Reactions.emojiUISet.shuffled().take(3)
                .map { it.copy(isSelected = Random.nextBoolean()) }
        ),
        MessageUI(
            userId = 2,
            messageId = 2,
            avatarUrl = null,
            username = "Me",
            message = "Привет! Уже почти закончил с RTL логикой. Выглядит круто!",
            timestamp = "12:05",
            streamName = "Android",
            topicName = "Compose Layouts",
            isOutgoingMessage = true,
            reactions = Reactions.emojiUISet.shuffled().take(3)
        ),
        MessageUI(
            userId = 1,
            messageId = 3,
            avatarUrl = null,
            username = "Kayden Hartman",
            message = "Супер, скидывай скрины в топик, когда закончишь.",
            timestamp = "12:10",
            streamName = "Android",
            topicName = "Compose Layouts",
            isOutgoingMessage = false,
            reactions = Reactions.emojiUISet.shuffled().take(3)
        )
    )

    ZulipTheme() {
        TopicScreen(
            viewModel = null,
            streamName = "test",
            topicName = "test",
            onBackClick = {},
        )
    }
}