package com.setjy.practiceapp.domain.usecase.stream

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.StreamWithTopics
import com.setjy.practiceapp.domain.repo.StreamRepo
import com.setjy.practiceapp.presentation.ui.channels.StreamItemUI
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

open class GetStreamsUseCase @Inject constructor(
    private val repo: StreamRepo,
    private val mapper: @JvmSuppressWildcards DomainMapper<StreamWithTopics, StreamItemUI>,
    private val scheduler: Scheduler
) :
    UseCase<GetStreamsUseCase.Params, Flowable<List<StreamItemUI>>> {

    data class Params(val isSubscribed: Boolean)

    override fun execute(params: Params): Flowable<List<StreamItemUI>> =
        repo.getStreams(params.isSubscribed)
            .subscribeOn(scheduler)
            .map { streamsDomain -> streamsDomain.map(mapper::mapToPresentation) }
}