package com.setjy.practiceapp.di

import com.setjy.practiceapp.data.local.db.dao.UserStorage
import com.setjy.practiceapp.data.local.storage.UserStorageImpl
import com.setjy.practiceapp.data.repo.UserRepoImpl
import com.setjy.practiceapp.domain.repo.UserRepo
import com.setjy.practiceapp.presentation.base.mvi.Middleware
import com.setjy.practiceapp.presentation.base.mvi.Reducer
import com.setjy.practiceapp.presentation.ui.profile.ProfileAction
import com.setjy.practiceapp.presentation.ui.profile.ProfileEffect
import com.setjy.practiceapp.presentation.ui.profile.ProfileReducer
import com.setjy.practiceapp.presentation.ui.profile.ProfileState
import com.setjy.practiceapp.presentation.ui.profile.middleware.LoadUserMiddleware
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
interface AppBindModule {
    @Binds
    fun bindUserRepo(userRepoImpl: UserRepoImpl): UserRepo

    @Binds
    fun bindUserStorage(userStorageImpl: UserStorageImpl): UserStorage

    @Binds
    fun bindProfileReducer(profileReducer: ProfileReducer): Reducer<ProfileAction, ProfileState, ProfileEffect>

    @Binds
    @IntoSet
    fun bindProfileMiddleware(loadUserMiddleware: LoadUserMiddleware): Middleware<ProfileState, ProfileAction>
}