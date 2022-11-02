package com.setjy.practiceapp.domain.usecase.message

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.repo.MessageRepo
import io.reactivex.rxjava3.core.Completable

open class SendMessageUseCase constructor(private val repo: MessageRepo) :
    UseCase<SendMessageUseCase.Params, Completable> {

    data class Params(val streamName: String, val topicName: String, val message: String)

    override fun execute(params: Params?): Completable =
        repo.sendMessage(
            streamName = params!!.streamName,
            topicName = params.topicName,
            message = params.message
        )
}