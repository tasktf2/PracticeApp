package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface MessageRepo {

    fun getLocalMessages(
        streamName: String,
        topicName: String
    ): Single<List<MessageWithReactionsDomain>>

    fun getRemoteMessages(
        streamName: String,
        topicName: String,
        anchor: String
    ): Observable<List<MessageWithReactionsDomain>>

    fun getMessagesOnLaunch(
        streamName: String,
        topicName: String
    ): Flowable<List<MessageWithReactionsDomain>>

    fun sendMessage(
        streamName: String,
        topicName: String,
        message: String
    ): Completable
}