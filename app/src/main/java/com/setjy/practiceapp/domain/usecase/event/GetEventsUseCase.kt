package com.setjy.practiceapp.domain.usecase.event

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.EventRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repo: EventRepo,
    private val mapper: @JvmSuppressWildcards DomainMapper<MessageWithReactionsDomain, MessageUI>,
    private val scheduler: Scheduler
) :
    UseCase<GetEventsUseCase.Params, Observable<Pair<List<MessageUI>, Int>>> {

    data class Params(
        val streamName: String,
        val topicName: String,
        val queueId: String,
        val lastEventId: Int
    )

    override fun execute(params: Params): Observable<Pair<List<MessageUI>, Int>> =
        repo.getEvents(
            streamName = params.streamName,
            topicName = params.topicName,
            queueId = params.queueId,
            lastEventId = params.lastEventId
        )
            .subscribeOn(scheduler)
            .map { messagesDomain -> messagesDomain.map(mapper::mapToPresentation) }
            .map { it to repo.getLastEventId() }
}