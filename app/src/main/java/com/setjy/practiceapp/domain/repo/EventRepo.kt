package com.setjy.practiceapp.domain.repo

import com.setjy.practiceapp.data.remote.response.GetEventRemote
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import io.reactivex.rxjava3.core.Observable

interface EventRepo {
    fun getEvents(streamName: String, topicName: String): Observable<List<MessageWithReactionsDomain>>
}