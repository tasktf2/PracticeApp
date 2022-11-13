package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.model.MessageUI

sealed class TopicAction : BaseAction {

    object ShowLoading : TopicAction()

    data class GetNewestMessages(val streamName: String, val topicName: String) : TopicAction()

    data class ShowMessages(val messages: List<MessageUI>) : TopicAction()

    object RegisterEventsQueue : TopicAction()

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

    data class DeleteReaction(val messageId: Int, val emojiName: String) : TopicAction()

    data class SendMessage(val streamName: String, val topicName: String, val message: String) :
        TopicAction()

    data class StartPagination(val streamName: String, val topicName: String, val anchor: Int) :
        TopicAction()

    data class ShowPaginationResult(
        val messagesFromScroll: List<MessageUI>,
        val isLastPage: Boolean
    ) : TopicAction()


    data class ShowError(val error: Throwable) : TopicAction()

    data class ShowBottomSheetFragment(val messageId:Int) : TopicAction()
}