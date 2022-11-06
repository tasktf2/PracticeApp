package com.setjy.practiceapp.domain.usecase.user

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GetAllUsersUseCase(
    private val repo: UserRepo,
    private val mapper: UserMapper,
    private val scheduler: Scheduler
) :
    UseCase<Unit, Observable<List<UserItemUI>>> {
    override fun execute(params: Unit): Observable<List<UserItemUI>> = repo.getAllUsers()
        .subscribeOn(scheduler)
        .flatMap { users ->
            Observable.fromIterable(users).flatMap { user ->
                repo.getUserStatus(user.userId).map(user::toDomain)
            }.toList().toObservable()
        }
        .map { usersDomain -> usersDomain.map(mapper::mapToPresentation) }
        .retryWhen { observableThrowable ->
            observableThrowable.flatMap { error ->
                if (error is UnknownHostException) {
                    Observable.timer(10, TimeUnit.SECONDS)
                } else {
                    observableThrowable
                }
            }
        }

}