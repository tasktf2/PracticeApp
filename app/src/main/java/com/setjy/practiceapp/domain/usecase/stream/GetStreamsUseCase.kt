package com.setjy.practiceapp.domain.usecase.stream

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.StreamMapper
import com.setjy.practiceapp.domain.repo.StreamRepo
import com.setjy.practiceapp.presentation.model.StreamItemUI
import io.reactivex.rxjava3.core.Flowable

open class GetStreamsUseCase constructor(
    private val repo: StreamRepo,
    private val mapper: StreamMapper
) :
    UseCase<GetStreamsUseCase.Params, Flowable<List<StreamItemUI>>> {


    override fun execute(params: Params?): Flowable<List<StreamItemUI>> =
        repo.getStreams(params!!.isSubscribed)
            .map { streamsDomain -> streamsDomain.map { mapper.mapToPresentation(it) } }

    data class Params(val isSubscribed: Boolean)
}