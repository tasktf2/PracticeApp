package com.setjy.practiceapp.domain.usecase.message

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.model.MessageWithReactionsDomain
import com.setjy.practiceapp.domain.repo.MessageRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

open class GetNewestMessagesUseCase @Inject constructor(
    private val repo: MessageRepo,
    private val mapper: @JvmSuppressWildcards DomainMapper<MessageWithReactionsDomain, MessageUI>,
    private val scheduler: Scheduler
) :
    UseCase<GetNewestMessagesUseCase.Params, Flowable<List<MessageUI>>> {

    data class Params(val streamName: String, val topicName: String)

    override fun execute(params: Params): Flowable<List<MessageUI>> =
        repo.getNewestMessages(streamName = params.streamName, topicName = params.topicName)
            .subscribeOn(scheduler)
            .map { messagesDomain -> messagesDomain.map(mapper::mapToPresentation) }
}