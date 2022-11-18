package com.setjy.practiceapp.data.local.storage

import com.setjy.practiceapp.data.local.db.dao.StreamDao
import com.setjy.practiceapp.data.local.model.StreamEntity
import com.setjy.practiceapp.data.local.model.StreamWithTopicsEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StreamStorage @Inject constructor(private val streamDao: StreamDao) {

    fun getStreams(isSubscribed: Boolean): Single<List<StreamWithTopicsEntity>> =
        Single.fromCallable { streamDao.getStreams(isSubscribed) }

    fun insertAllStreams(streams: List<StreamEntity>) = streamDao.insertAllStreams(streams)
}