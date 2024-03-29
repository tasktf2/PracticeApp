package com.setjy.practiceapp.data.local.storage

import com.setjy.practiceapp.data.local.db.dao.TopicDao
import com.setjy.practiceapp.data.local.model.TopicEntity
import javax.inject.Inject

class TopicStorage @Inject constructor(private val topicDao: TopicDao) {
    fun insertTopics(topics: List<TopicEntity>) = topicDao.insertTopics(topics)
}