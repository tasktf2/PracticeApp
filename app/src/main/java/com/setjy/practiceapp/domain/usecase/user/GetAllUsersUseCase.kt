package com.setjy.practiceapp.domain.usecase.user

import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.presentation.model.UserItemUI
import io.reactivex.rxjava3.core.Observable

class GetAllUsersUseCase constructor(private val repo: UserRepo, private val mapper: UserMapper)
    :
    UseCase<Unit, Observable<List<UserItemUI>>> {
//{
    override
    fun execute(
    params: Unit?
): Observable<List<UserItemUI>> = repo.getAllUsers()
        .flatMap {
            Observable.fromIterable(it).flatMap { user ->
                repo.getUserStatus(user.userId).map { status -> user.toDomain(status) }
            }.toList().toObservable()
        }.map { usersDomain -> usersDomain.map { mapper.mapToPresentation(it) } }
    //todo handle network ex?
}