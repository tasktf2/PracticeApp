package com.setjy.practiceapp.di.profile

import android.content.SharedPreferences
import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.local.storage.UserStorageImpl
import com.setjy.practiceapp.data.remote.api.UsersApi
import com.setjy.practiceapp.data.repo.UserRepoImpl
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.UserDomain
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.base.mvi.Store
import com.setjy.practiceapp.presentation.ui.profile.*
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler

@Module
class ProfileModule {

    @Provides
    @ProfileScope
    fun provideUserRepo(api: UsersApi, storage: UserStorage): UserRepo = UserRepoImpl(api, storage)

    @Provides
    @ProfileScope
    fun provideUserStorage(prefs: SharedPreferences): UserStorage = UserStorageImpl(prefs)

    @Provides
    @ProfileScope
    fun provideProfileReducer(): Reducer<ProfileAction, ProfileState, ProfileEffect> = ProfileReducer()

    @Provides
    @ProfileScope
    fun provideUserMapper(): DomainMapper<UserDomain, UserItemUI> = UserMapper()

    @Provides
    @ProfileScope
    fun provideOwnUserUsecase(
        repo: UserRepo,
        mapper: @JvmSuppressWildcards DomainMapper<UserDomain, UserItemUI>,
        scheduler: Scheduler
    ): UseCase<Unit, Observable<UserItemUI>> = GetOwnUserUseCase(repo, mapper, scheduler)

    @Provides
    @ProfileScope
    @IntoSet
    fun provideUserMiddleware(
        useCase: @JvmSuppressWildcards UseCase<Unit, Observable<UserItemUI>>
    ): Middleware<ProfileState, ProfileAction> = LoadUserMiddleware(useCase)

    @Provides
    fun provideProfileState(): ProfileState = ProfileState(userItemUI = null, error = null, isLoading = false)

    @Provides
    @ProfileScope
    fun provideProfileStore(
        reducer: @JvmSuppressWildcards Reducer<ProfileAction, ProfileState, ProfileEffect>,
        middlewares: @JvmSuppressWildcards Set<Middleware<ProfileState, ProfileAction>>,
        initialState: ProfileState
    ): Store<ProfileAction, ProfileState, ProfileEffect> = Store(reducer, middlewares, initialState)

}