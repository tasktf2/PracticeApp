package com.setjy.practiceapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.setjy.practiceapp.data.local.db.dao.MessageDao
import com.setjy.practiceapp.data.local.db.dao.ReactionDao
import com.setjy.practiceapp.data.local.db.dao.StreamDao
import com.setjy.practiceapp.data.local.db.dao.TopicDao
import com.setjy.practiceapp.data.model.MessageEntity
import com.setjy.practiceapp.data.model.ReactionEntity
import com.setjy.practiceapp.data.model.StreamEntity
import com.setjy.practiceapp.data.model.TopicEntity

@Database(
    entities = [
        StreamEntity::class,
        TopicEntity::class,
        MessageEntity::class,
        ReactionEntity::class
    ], version = 1
)
abstract class ZulipDatabase : RoomDatabase() {

    abstract fun streamDao(): StreamDao

    abstract fun topicDao(): TopicDao

    abstract fun messageDao(): MessageDao

    abstract fun reactionDao(): ReactionDao
}