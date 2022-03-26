package com.setjy.practiceapp

import com.setjy.practiceapp.recycler.base.ViewTyped
import com.setjy.practiceapp.recycler.holders.IncomingMessageUI
import com.setjy.practiceapp.recycler.holders.OutgoingMessageUI
import com.setjy.practiceapp.recycler.holders.TimeUI
import com.setjy.practiceapp.recycler.reactions.EmojiNCS

const val DEFAULT_USER_ID: String = "1"

data class EmojiRemote(
    val code: String,
    val userId: String
)

object Data {

    private val listOfRemoteReactions = listOf(
        EmojiRemote("1f600", DEFAULT_USER_ID),
        EmojiRemote("1f600", "3"),
        EmojiRemote("1f600", "2"),

        EmojiRemote("1f603", DEFAULT_USER_ID),
        EmojiRemote("1f603", "3"),

        EmojiRemote("1f604", "4"),
        EmojiRemote("1f601", "5"),
        EmojiRemote("1f606", "6"),
        EmojiRemote("1f605", DEFAULT_USER_ID)
    )

    private val listOfReactions = listOfRemoteReactions.map {
        EmojiNCS(
            code = it.code,
            isSelected = it.userId == DEFAULT_USER_ID
        )
    }

    fun getMessages(): List<ViewTyped> =
        listOf(
            TimeUI(System.currentTimeMillis()),
            OutgoingMessageUI(text = "getString(R.string.test_message_text)"),
            IncomingMessageUI(
                message_id = "1",
                avatar = R.drawable.ic_launcher_background,
                username = "Denis Mashkov",
                message = "getString(R.string.test_message_text)",
                reactions = listOfReactions
            )

        ).asReversed()

}