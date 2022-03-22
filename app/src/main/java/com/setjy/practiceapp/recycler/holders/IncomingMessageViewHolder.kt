package com.setjy.practiceapp.recycler.holders

import android.util.Log
import android.view.View
import com.setjy.practiceapp.ChatFragment
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.holders.OutgoingMessageViewHolder.Companion.DEFAULT_USER_ID
import com.setjy.practiceapp.recycler.reactions.EmojiNCS
import com.setjy.practiceapp.recycler.reactions.Reaction
import com.setjy.practiceapp.view.CustomViewGroup
import com.setjy.practiceapp.view.EmojiView

data class IncomingMessageUI(
    val message_id: String,
    val avatar: Int,
    val username: String,
    val message: String,
    override val viewType: Int = R.layout.item_msg_incoming,
) : ViewTyped

class IncomingMessageViewHolder(
    val view: View, longClick: (View) -> Unit
) : BaseViewHolder<IncomingMessageUI>(view) {

    private val messageView: CustomViewGroup = view as CustomViewGroup

    init {
        view.setOnLongClickListener {
            longClick(it)
            true
        }
    }

    override fun bind(item: IncomingMessageUI) {
        with(messageView.binding) {
            ivAvatar.setImageResource(item.avatar)
            tvUsername.text = item.username
            tvMessage.text = item.message
            //это костыль
            messageId.text = item.message_id

            with(ChatFragment) {
                if (listOfReactions.any { it.message_id == messageId.text }) {
                    val mapOfReactions =
                        listOfReactions.find { it.message_id == messageId.text }!!.mapOfReactions
                    mapOfReactions.forEach { addEmojiViews() }
                }

            }
        }
    }

    private fun addEmojiViews() {
        with(messageView.binding) {

            val messageIdString = messageId.text.toString()
            if (ChatFragment.listOfReactions.any { it.message_id == messageIdString }) {
                val mapOfReactions =
                    ChatFragment.listOfReactions.find { it.message_id == messageIdString }!!.mapOfReactions
                mapOfReactions.forEach { mapItem ->

                    flexbox.addView(EmojiView(root.context).apply {
                        emojiUnicode = mapItem.key.getCodeString()
                        emojiCounter = mapItem.value.size
                        flagIsSelected = true
                        setOnClickListener {
                            toggleReactionInList(messageView, messageIdString, mapItem.key, this)
                        }
                    })
                }
            }
        }
    }

    private fun addReactionInList(messageId: String, currentEmojiNCS: EmojiNCS) {

        with(ChatFragment) {
            val currentReactionMap =
                listOfReactions.find { it.message_id == messageId }!!.mapOfReactions
            when {
                listOfReactions.isNullOrEmpty() -> {
                    listOfReactions.add(
                        Reaction(
                            messageId, mutableMapOf(
                                currentEmojiNCS to mutableListOf(DEFAULT_USER_ID)
                            )
                        )
                    )
                }
                !currentReactionMap.containsKey(currentEmojiNCS) -> {
                    currentReactionMap.put(
                        currentEmojiNCS,
                        mutableListOf(DEFAULT_USER_ID)
                    )
                }
                else -> {
                    currentReactionMap[currentEmojiNCS]?.add(DEFAULT_USER_ID)
                }
            }
        }
    }

    private fun toggleReactionInList(
        incomeMessage: CustomViewGroup,
        messageId: String,
        currentEmojiNCS: EmojiNCS,
        emojiView: EmojiView
    ) {
        val currentUsersReactionList =
            ChatFragment.listOfReactions.find { it.message_id == messageId }
                ?.mapOfReactions?.get(currentEmojiNCS)
        if (emojiView.flagIsSelected && currentUsersReactionList
                ?.contains(DEFAULT_USER_ID) == true
        ) {
            currentUsersReactionList.removeIf { it == DEFAULT_USER_ID }
            emojiView.apply {
                flagIsSelected = false
                emojiCounter = currentUsersReactionList.size
                invalidate()
            }
            Log.d("xxx", "removed reaction")

            if (currentUsersReactionList.isEmpty()) {
                incomeMessage.binding.flexbox.apply {
                    removeView(emojiView)
                }
            }
        } else {
            addReactionInList(messageId, currentEmojiNCS)
            if (currentUsersReactionList != null) {
                emojiView.apply {
                    emojiCounter = currentUsersReactionList.size
                    emojiView.flagIsSelected = true
                    invalidate()
                }
            }
            Log.d("xxx", "added reaction")
        }
    }
}