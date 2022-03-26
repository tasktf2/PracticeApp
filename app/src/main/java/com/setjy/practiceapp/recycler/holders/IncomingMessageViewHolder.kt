package com.setjy.practiceapp.recycler.holders

import android.view.View
import com.setjy.practiceapp.R
import com.setjy.practiceapp.recycler.base.BaseViewHolder
import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.reactions.EmojiNCS
import com.setjy.practiceapp.view.CustomViewGroup

//todo move to items
data class IncomingMessageUI(
    val message_id: String,
    val avatar: Int,
    val username: String,
    val message: String,
    val reactions: List<EmojiNCS>,
    override val viewType: Int = R.layout.item_msg_incoming,
) : ViewTyped

class IncomingMessageViewHolder(view: View) : BaseViewHolder<IncomingMessageUI>(view) {

    var currentMessageId: String? = null

    var onEmojiClick: ((messageId: String, emojiCode: String) -> Unit)? = null

    var onAddEmojiClick: ((messageId: String) -> Unit)? = null

    private val messageView: CustomViewGroup = view as CustomViewGroup

    override fun bind(item: IncomingMessageUI) {
        currentMessageId = item.message_id
        with(messageView.binding) {
            ivAvatar.setImageResource(item.avatar)
            tvUsername.text = item.username
            tvMessage.text = item.message
        }
        messageView.setEmojis(item.reactions)
        messageView.setOnAddEmojiClickListener {
            onAddEmojiClick?.invoke(item.message_id)
        }
        messageView.setOnEmojiClickListener { emojiCode ->
            onEmojiClick?.invoke(item.message_id, emojiCode)
        }
    }

//    private fun addEmojiViews() {
//        with(messageView.binding) {
//
//            val messageIdString = messageId.text.toString()
//            if (ChatFragment.listOfReactions.any { it.message_id == messageIdString }) {
//                val mapOfReactions =
//                    ChatFragment.listOfReactions.find { it.message_id == messageIdString }!!.mapOfReactions
//                mapOfReactions.forEach { mapItem ->
//
//                    flexbox.addView(EmojiView(root.context).apply {
//                        emojiUnicode = mapItem.key.getCodeString()
//                        emojiCounter = mapItem.value.size
//                        flagIsSelected = true
//                        setOnClickListener {
//                            toggleReactionInList(messageView, messageIdString, mapItem.key, this)
//                        }
//                    })
//                }
//            }
//        }
//    }

//    private fun addReactionInList(messageId: String, currentEmojiNCS: EmojiNCS) {
//
//        with(ChatFragment) {
//            val currentReactionMap =
//                listOfReactions.find { it.message_id == messageId }!!.mapOfReactions
//            when {
//                listOfReactions.isNullOrEmpty() -> {
//                    listOfReactions.add(
//                        Reaction(
//                            messageId, mutableMapOf(
//                                currentEmojiNCS to mutableListOf(DEFAULT_USER_ID)
//                            )
//                        )
//                    )
//                }
//                !currentReactionMap.containsKey(currentEmojiNCS) -> {
//                    currentReactionMap.put(
//                        currentEmojiNCS,
//                        mutableListOf(DEFAULT_USER_ID)
//                    )
//                }
//                else -> {
//                    currentReactionMap[currentEmojiNCS]?.add(DEFAULT_USER_ID)
//                }
//            }
//        }
//    }

//    private fun toggleReactionInList(
//        incomeMessage: CustomViewGroup,
//        messageId: String,
//        currentEmojiNCS: EmojiNCS,
//        emojiView: EmojiView
//    ) {
//        val currentUsersReactionList =
//            ChatFragment.listOfReactions.find { it.message_id == messageId }
//                ?.mapOfReactions?.get(currentEmojiNCS)
//        if (emojiView.flagIsSelected && currentUsersReactionList
//                ?.contains(DEFAULT_USER_ID) == true
//        ) {
//            currentUsersReactionList.removeIf { it == DEFAULT_USER_ID }
//            emojiView.apply {
//                flagIsSelected = false
//                emojiCounter = currentUsersReactionList.size
//                invalidate()
//            }
//            Log.d("xxx", "removed reaction")
//
//            if (currentUsersReactionList.isEmpty()) {
//                incomeMessage.binding.flexbox.apply {
//                    removeView(emojiView)
//                }
//            }
//        } else {
//            addReactionInList(messageId, currentEmojiNCS)
//            if (currentUsersReactionList != null) {
//                emojiView.apply {
//                    emojiCounter = currentUsersReactionList.size
//                    emojiView.flagIsSelected = true
//                    invalidate()
//                }
//            }
//            Log.d("xxx", "added reaction")
//        }
//    }
}