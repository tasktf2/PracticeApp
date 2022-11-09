package com.setjy.practiceapp.domain.usecase.message

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.repo.MessageRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler

open class GetNewestMessagesUseCase(
    private val repo: MessageRepo,
    private val mapper: MessageMapper,
    private val scheduler: Scheduler
) :
    UseCase<GetNewestMessagesUseCase.Params, Flowable<List<MessageUI>>> {

    data class Params(val streamName: String, val topicName: String)

    override fun execute(params: Params): Flowable<List<MessageUI>> =
        repo.getNewestMessages(streamName = params.streamName, topicName = params.topicName)
            .subscribeOn(scheduler)
            .map { messagesDomain -> messagesDomain.map(mapper::mapToPresentation) }
}