package com.setjy.practiceapp.domain.usecase.message

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.MessageRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

open class PaginationUseCase @Inject constructor(
    private val repo: MessageRepo,
    private val mapper: @JvmSuppressWildcards DomainMapper<MessageWithReactionsDomain, MessageUI>,
    private val scheduler: Scheduler
) :
    UseCase<PaginationUseCase.Params, Observable<List<MessageUI>>> {

    data class Params(val streamName: String, val topicName: String, val anchor: String)

    override fun execute(params: Params): Observable<List<MessageUI>> =
        repo.getRemoteMessages(
            streamName = params.streamName,
            topicName = params.topicName,
            anchor = params.anchor
        ).subscribeOn(scheduler)
            .map { messagesDomain -> messagesDomain.map(mapper::mapToPresentation) }
}