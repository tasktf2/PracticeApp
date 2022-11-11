package com.setjy.practiceapp.domain.usecase.event

import com.setjy.practiceapp.data.remote.response.SendEventResponse
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.repo.EventRepo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler

class RegisterEventsQueueUseCase(private val repo: EventRepo, private val scheduler: Scheduler) :
    UseCase<Unit, Observable<SendEventResponse>> {

    override fun execute(params: Unit): Observable<SendEventResponse> = repo.subscribeToEvents()
        .subscribeOn(scheduler)
}