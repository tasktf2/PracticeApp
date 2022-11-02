package com.setjy.practiceapp.data.local.storage

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.R
import com.setjy.practiceapp.presentation.model.TopicItemUI
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.model.TopicEntity
import io.reactivex.rxjava3.core.Single

class TopicStorage constructor(context: Context) {
    private val database: ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        context.getString(R.string.database_name)
    ).build()

    private val topicDao = database.topicDao()

    fun getTopicsByStreamId(streamId: Int): Single<List<TopicEntity>> =
        Single.fromCallable { topicDao.getTopicsByStreamId(streamId) }

    fun insertTopics(topics: List<TopicEntity>) = topicDao.insertTopics(topics)

    companion object {
        private var STORAGE_INSTANCE: TopicStorage? = null
        fun initialize(context: Context) {
            if (STORAGE_INSTANCE == null) STORAGE_INSTANCE = TopicStorage(context)
        }

        fun get(): TopicStorage {
            return STORAGE_INSTANCE
                ?: throw IllegalStateException("TopicsStorage must be initialized")
        }
    }
}