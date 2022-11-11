package com.setjy.practiceapp.presentation.ui.topic

import com.setjy.practiceapp.presentation.base.mvi.BaseAction
import com.setjy.practiceapp.presentation.model.MessageUI

sealed class TopicAction : BaseAction {

    object ShowLoading : TopicAction()

    object GetNewestMessages : TopicAction()

    class ShowMessages(val messages: List<MessageUI>) : TopicAction()

    object RegisterEventsQueue : TopicAction()

    object QueueRegistered : TopicAction()

    object GetEvents : TopicAction()

    class AddReaction(val messageId: Int, val emojiName: String) : TopicAction()

    class DeleteReaction(val messageId: Int, val emojiName: String) : TopicAction()

    class SendMessage(val message: String) : TopicAction()

    class GetMessagesOnScroll(val anchor: Int) : TopicAction()

    class ShowMessagesOnScroll(
        val messagesFromScroll: List<MessageUI>,
        val onScrollIsLoading: Boolean,
        val onScrollIsLastPage: Boolean
    ) : TopicAction()

    class ShowError(val error: Throwable) : TopicAction()

    object ActionSent : TopicAction()
}