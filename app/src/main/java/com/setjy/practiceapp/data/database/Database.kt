package com.setjy.practiceapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.setjy.practiceapp.data.network.EmojiRemote
import com.setjy.practiceapp.recycler.items.MessageDB
import com.setjy.practiceapp.recycler.items.StreamItemUI
import com.setjy.practiceapp.recycler.items.TopicItemUI
import com.setjy.practiceapp.recycler.items.UserItemUI

@Database(
    entities = [
        StreamItemUI::class,
        TopicItemUI::class,
        MessageDB::class,
        UserItemUI::class,
        EmojiRemote::class
    ], version = 1
)
abstract class ZulipDatabase : RoomDatabase() {

    abstract fun streamDao(): StreamDao

    abstract fun topicDao(): TopicDao

    abstract fun messagesDao(): MessagesDao

    abstract fun reactionsDao(): ReactionsDao

    abstract fun usersDao():UsersDao
}