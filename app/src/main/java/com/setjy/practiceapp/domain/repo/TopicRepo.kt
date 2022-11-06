package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.data.remote.response.TopicsRemote
import io.reactivex.rxjava3.core.Observable

interface TopicRepo {
    fun getRemoteTopics(streamId: Int): Observable<List<TopicsRemote>>
}