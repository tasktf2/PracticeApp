package com.setjy.practiceapp.domain.usecase.message

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.MessageMapper
import com.setjy.practiceapp.domain.repo.MessageRepo
import com.setjy.practiceapp.presentation.model.MessageUI
import io.reactivex.rxjava3.core.Flowable

open class GetMessagesOnLaunchUseCase constructor(
    private val repo: MessageRepo,
    private val mapper: MessageMapper
) :
    UseCase<GetMessagesOnLaunchUseCase.Params, Flowable<List<MessageUI>>> {

    data class Params(val streamName: String, val topicName: String)

    override fun execute(params: Params?): Flowable<List<MessageUI>> =
        repo.getMessagesOnLaunch(
            streamName = params!!.streamName,
            topicName = params.topicName
        ).map { messagesDomain -> messagesDomain.map { mapper.mapToPresentation(it) } }
}