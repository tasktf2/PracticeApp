package com.setjy.practiceapp.presentation.ui.topic

import androidx.compose.runtime.Immutable
import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.model.MessageUI

sealed class TopicAction : BaseAction {

    object ShowLoading : TopicAction()

    data class GetNewestMessages(val streamName: String, val topicName: String) : TopicAction()

    data class ShowMessages(val messages: List<MessageUI>) : TopicAction()

    object RegisterEventsQueue : TopicAction()
    object DeleteSearch : TopicAction()
    data class QueueRegistered(val queueId: String, val lastEventId: Int) : TopicAction()

    data class GetEvents(
        val streamName: String,
        val topicName: String,
        val queueId: String,
        val lastEventId: Int
    ) : TopicAction()

    data class ShowEvents(
        val messages: List<MessageUI>,
        val queueId: String,
        val lastEventId: Int
    ) : TopicAction()

    data class AddReaction(val messageId: Int, val emojiName: String) : TopicAction()
    data class TypeMessage(val text: String) : TopicAction()

    data class DeleteReaction(val messageId: Int, val emojiName: String) : TopicAction()

    data class SendMessage(val streamName: String, val topicName: String) :
        TopicAction()

    data class StartPagination(val streamName: String, val topicName: String) :
        TopicAction()

    data class ShowPaginationResult(
        val messagesFromScroll: List<MessageUI>,
        val isLastPage: Boolean
    ) : TopicAction()


    data class ShowError(val error: Throwable) : TopicAction()
    data class ShowErrorAndReturnMessage(val error: Throwable, val message: String) : TopicAction()

    data class ShowBottomSheet(val messageId: Int) : TopicAction()
    data object HideBottomSheet : TopicAction()
    data class AcceptSearchAction(val action: SearchAction) : TopicAction()
    class SearchChanged(val search: String) : TopicAction()

    data class FoundIndices(val indices: List<Int>) : TopicAction()

    data class EmojiClicked(val messageId: Int, val emojiName: String, val emojiCode: String) :
        TopicAction()

    object DeleteMessageText : TopicAction()
}

@Immutable
class TopicActions(
    val onEmojiClick: (Int, String, String) -> Unit,
    val onLongClick: (Int) -> Unit,
    val onSendMessage: () -> Unit,
    val onBackClick: () -> Unit,
    val onSearchChanged: (String) -> Unit,
    val onDeleteClick: () -> Unit,
    val onSearchNext: () -> Unit,
    val onSearchPrev: () -> Unit,
    val onSearchStart: () -> Unit,
    val onType: (String) -> Unit
)