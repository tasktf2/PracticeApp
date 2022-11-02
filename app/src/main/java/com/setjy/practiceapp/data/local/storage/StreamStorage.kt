package com.setjy.practiceapp.data.local.storage

import android.content.Context
import androidx.room.Room
import com.setjy.practiceapp.R
import com.setjy.practiceapp.data.local.db.ZulipDatabase
import com.setjy.practiceapp.data.local.db.dao.StreamDao
import com.setjy.practiceapp.data.model.StreamEntity
import com.setjy.practiceapp.data.model.StreamWithTopicsEntity
import io.reactivex.rxjava3.core.Single

class StreamStorage constructor(context: Context) {
    private val database: ZulipDatabase = Room.databaseBuilder(
        context.applicationContext,
        ZulipDatabase::class.java,
        context.getString(R.string.database_name)
    ).build()

    private val streamDao = database.streamDao()

    fun getStreams(isSubscribed: Boolean): Single<List<StreamWithTopicsEntity>> =
        Single.fromCallable { streamDao.getStreams(isSubscribed) }

    fun insertAllStreams(streams: List<StreamEntity>) =
        streamDao.insertAllStreams(streams)

    companion object {
        private var STORAGE_INSTANCE: StreamStorage? = null
        fun initialize(context: Context) {
            if (STORAGE_INSTANCE == null) STORAGE_INSTANCE = StreamStorage(context)
        }

        fun get(): StreamStorage {
            return STORAGE_INSTANCE
                ?: throw IllegalStateException("StreamsStorage must be initialized")
        }
    }

}