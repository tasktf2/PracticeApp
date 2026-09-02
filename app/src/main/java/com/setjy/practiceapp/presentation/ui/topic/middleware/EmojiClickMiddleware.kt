package com.setjy.practiceapp.presentation.ui.topic.middleware

import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.topic.TopicAction
import com.setjy.practiceapp.presentation.ui.topic.TopicState
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EmojiClickMiddleware @Inject constructor() :
    Middleware<TopicState, TopicAction> {
    override fun bind(
        actions: Observable<TopicAction>,
        state: Observable<TopicState>
    ): Observable<TopicAction> {
        return actions.ofType(TopicAction.EmojiClicked::class.java)
            .withLatestFrom(state) { action, state ->
                val message = state.messages.orEmpty().firstOrNull { it.messageId == action.messageId }

                val mutableReactions = message?.reactions.orEmpty().toMutableList()
                val isRemoved =
                    mutableReactions.removeIf { it.code == action.emojiCode && it.isSelected }

                if (isRemoved) {
                    TopicAction.DeleteReaction(action.messageId, action.emojiName)
                } else {
                    TopicAction.AddReaction(action.messageId, action.emojiName)
                }
            }
    }
}
