package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.data.remote.response.SendEventResponse
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import io.reactivex.rxjava3.core.Observable

interface EventRepo {

    fun subscribeToEvents(): Observable<SendEventResponse>

    fun getEvents(
        streamName: String,
        topicName: String,
        queueId: String,
        lastEventId: Int
    ): Observable<List<MessageWithReactionsDomain>>

    fun getLastEventId():Int
}