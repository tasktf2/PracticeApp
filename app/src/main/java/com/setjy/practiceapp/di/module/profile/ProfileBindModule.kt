package com.setjy.practiceapp.di.module.profile

import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.local.storage.UserStorageImpl
import com.setjy.practiceapp.data.repo.UserRepoImpl
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.UseCase
import com.setjy.practiceapp.domain.model.UserDomain
import com.setjy.practiceapp.domain.model.UserMapper
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.domain.usecase.user.GetOwnUserUseCase
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import dagger.Binds
import dagger.Module
import io.reactivex.rxjava3.core.Observable

@Module
interface ProfileBindModule {

    @Binds
    @ProfileScope
    fun provideUserRepo(userRepoImpl: UserRepoImpl): UserRepo

    @Binds
    @ProfileScope
    fun provideUserStorage(userStorageImpl: UserStorageImpl): UserStorage

    @Binds
    @ProfileScope
    fun provideUserMapper(userMapper: UserMapper): DomainMapper<UserDomain, UserItemUI>

    @Binds
    @ProfileScope
    fun provideOwnUserUseCase(getOwnUserUseCase: GetOwnUserUseCase): UseCase<Unit, Observable<UserItemUI>>
}