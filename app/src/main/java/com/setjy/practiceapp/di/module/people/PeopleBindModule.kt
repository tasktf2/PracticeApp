package com.setjy.practiceapp.di.module.people

import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.local.storage.UserStorageImpl
import com.setjy.practiceapp.data.repo.UserRepoImpl
import com.setjy.practiceapp.di.scope.PeopleScope
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.UserDomain
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.domain.usecase.user.GetAllUsersUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.ui.people.PeopleAction
import com.setjy.practiceapp.presentation.ui.people.PeopleEffect
import com.setjy.practiceapp.presentation.ui.people.PeopleReducer
import com.setjy.practiceapp.presentation.ui.people.PeopleState
import com.setjy.practiceapp.presentation.ui.people.middleware.LoadUsersMiddleware
import com.setjy.practiceapp.presentation.ui.people.middleware.SearchUsersMiddleware
import com.setjy.practiceapp.presentation.ui.profile.ProfileAction
import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import dagger.Binds
import dagger.Module
import dagger.multibindings.ElementsIntoSet
import io.reactivex.rxjava3.core.Observable

@Module
interface PeopleBindModule {

    @Binds
    @PeopleScope
    fun bindUserRepo(userRepoImpl: UserRepoImpl): UserRepo

    @Binds
    @PeopleScope
    fun bindUserStorage(userStorageImpl: UserStorageImpl): UserStorage

    @Binds
    @PeopleScope
    fun bindUserMapper(userMapper: UserMapper): DomainMapper<UserDomain, UserItemUI>

    @Binds
    @PeopleScope
    fun bindPeopleReducer(peopleReducer: PeopleReducer): Reducer<PeopleAction, PeopleState, PeopleEffect>
}