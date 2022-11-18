package com.setjy.practiceapp.data.repo

import com.setjy.practiceapp.data.local.model.StreamWithTopicsEntityMapper
import com.setjy.practiceapp.data.local.storage.StreamStorage
import com.setjy.practiceapp.data.local.storage.TopicStorage
import com.setjy.practiceapp.data.remote.api.StreamsApi
import com.setjy.practiceapp.data.remote.response.StreamRemote
import com.setjy.practiceapp.data.remote.response.TopicsRemote
import com.setjy.practiceapp.domain.model.StreamWithTopics
import com.setjy.practiceapp.domain.repo.StreamRepo
import com.setjy.practiceapp.domain.repo.TopicRepo
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StreamRepoImpl @Inject constructor(
    private val api: StreamsApi,
    private val streamStorage: StreamStorage,
    private val topicStorage: TopicStorage,
    private val topicRepo: TopicRepo,
    private val mapper: StreamWithTopicsEntityMapper
) : StreamRepo {

    override fun getRemoteStreams(isSubscribed: Boolean): Observable<List<StreamWithTopics>> =
        if (isSubscribed) {
            api.getSubscribedStreams()
        } else {
            api.getAllStreams()
        }.flatMap {
            Observable.fromIterable(it.streams).flatMap { streamRemote ->
                Observable.zip(
                    Observable.just(streamRemote),
                    topicRepo.getRemoteTopics(streamRemote.streamId)
                ) { stream, topics -> remoteToDomain(stream, topics, isSubscribed) }
            }
        }
            .toList().toObservable()
            .doAfterNext { streamsDomain ->
                streamStorage.insertAllStreams(streamsDomain.map { mapper.mapToEntity(it).streamEntity })
                streamsDomain.map { topicStorage.insertTopics(mapper.mapToEntity(it).topics) }
            }
            .retryWhen { observableThrowable ->
                observableThrowable.flatMap { error ->
                    if (error is UnknownHostException) {
                        Observable.timer(10, TimeUnit.SECONDS)
                    } else {
                        observableThrowable
                    }
                }
            }

    private fun remoteToDomain(
        streamRemote: StreamRemote,
        topicsRemote: List<TopicsRemote>,
        isSubscribed: Boolean
    ): StreamWithTopics =
        StreamWithTopics(
            streamId = streamRemote.streamId,
            streamName = streamRemote.streamName,
            isSubscribed = isSubscribed,
            backgroundColor = streamRemote.streamColor,
            topics = topicsRemote.map { it.toDomain(streamRemote.streamId) }
        )

    override fun getLocalStreams(isSubscribed: Boolean): Single<List<StreamWithTopics>> =
        streamStorage.getStreams(isSubscribed).map { list -> list.map(mapper::mapToDomain) }

    override fun getStreams(isSubscribed: Boolean): Flowable<List<StreamWithTopics>> =
        Single.concatArrayEager(
            getLocalStreams(isSubscribed),
            Single.fromObservable(getRemoteStreams(isSubscribed))
        )
}
