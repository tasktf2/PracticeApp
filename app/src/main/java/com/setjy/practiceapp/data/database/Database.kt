package com.setjy.practiceapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.setjy.practiceapp.data.database.dao.*
import com.setjy.practiceapp.data.database.entity.MessageDB
import com.setjy.practiceapp.data.database.entity.StreamItemDB
import com.setjy.practiceapp.data.database.entity.TopicItemUI
import com.setjy.practiceapp.data.network.response.EmojiRemote

@Database(
    entities = [
        StreamItemDB::class,
        TopicItemUI::class,
        MessageDB::class,
        EmojiRemote::class
    ], version = 1
)
abstract class ZulipDatabase : RoomDatabase() {

    abstract fun streamDao(): StreamDao

    abstract fun topicDao(): TopicDao

    abstract fun messagesDao(): MessagesDao

    abstract fun reactionsDao(): ReactionsDao
}