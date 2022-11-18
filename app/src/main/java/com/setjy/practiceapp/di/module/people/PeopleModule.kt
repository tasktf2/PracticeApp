package com.setjy.practiceapp.di.module.people

import com.setjy.practiceapp.data.remote.api.UsersApi
import com.setjy.practiceapp.di.scope.PeopleScope
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.usecase.user.GetAllUsersUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.ui.people.PeopleAction
import com.setjy.practiceapp.presentation.ui.people.PeopleState
import com.setjy.practiceapp.presentation.ui.people.middleware.LoadUsersMiddleware
import com.setjy.practiceapp.presentation.ui.people.middleware.SearchUsersMiddleware
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit

@Module
class PeopleModule {

    @Provides
    @PeopleScope
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    @PeopleScope
    fun provideAllUsersUseCase(
        getAllUsersUseCase: GetAllUsersUseCase
    ): UseCase<Unit, Observable<List<UserItemUI>>> = getAllUsersUseCase

    @Provides
    @PeopleScope
    @ElementsIntoSet
    fun providePeopleMiddlewares(
        useCase: @JvmSuppressWildcards UseCase<Unit, Observable<List<UserItemUI>>>
    ): Set<Middleware<PeopleState, PeopleAction>> =
        setOf(LoadUsersMiddleware(useCase), SearchUsersMiddleware())

    @Provides
    fun providePeopleInitialState(): PeopleState = PeopleState()
}