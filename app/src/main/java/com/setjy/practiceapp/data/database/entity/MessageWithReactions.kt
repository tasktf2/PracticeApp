package com.setjy.practiceapp.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.setjy.practiceapp.data.network.response.EmojiRemote

data class MessageWithReactionsDB(
    @Embedded val messageDB: MessageDB,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "message_id"
    ) val reactions: List<EmojiRemote>

)