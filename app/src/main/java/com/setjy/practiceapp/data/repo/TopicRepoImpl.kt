package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.remote.NetworkService
import com.setjy.practiceapp.data.remote.api.TopicsApi
import com.setjy.practiceapp.data.remote.response.TopicsRemote
import com.setjy.practiceapp.domain.repo.TopicRepo
import io.reactivex.rxjava3.core.Observable
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class TopicRepoImpl constructor(private val api: TopicsApi) : TopicRepo {

    override fun getRemoteTopics(streamId: Int): Observable<List<TopicsRemote>> =
        api.getTopicsById(streamId).map { it.topics }
}