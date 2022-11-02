package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.domain.model.StreamWithTopics
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface StreamRepo {
    fun getRemoteStreams(isSubscribed: Boolean): Observable<List<StreamWithTopics>>

    fun getLocalStreams(isSubscribed: Boolean): Single<List<StreamWithTopics>>

    fun getStreams(isSubscribed: Boolean): Flowable<List<StreamWithTopics>>
}