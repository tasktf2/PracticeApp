package com.setjy.practiceapp.domain.usecase.event

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.repo.EventRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler

class GetEventsUseCase(
    private val repo: EventRepo, private val mapper: MessageMapper, private val scheduler: Scheduler
) :
    UseCase<GetEventsUseCase.Params, Observable<List<MessageUI>>> {

    data class Params(val streamName: String, val topicName: String)

    override fun execute(params: Params): Observable<List<MessageUI>> =
        repo.getEvents(streamName = params.streamName, topicName = params.topicName)
            .subscribeOn(scheduler)
            .map { messagesDomain -> messagesDomain.map(mapper::mapToPresentation) }
}