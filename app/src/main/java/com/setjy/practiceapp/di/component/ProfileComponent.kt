package com.setjy.practiceapp.di.component

import com.setjy.practiceapp.di.module.profile.ProfileBindModule
import com.setjy.practiceapp.di.module.profile.ProfileModule
import com.setjy.practiceapp.di.scope.ProfileScope
import com.setjy.practiceapp.presentation.ui.profile.ProfileFragment
import dagger.Subcomponent
import dagger.Subcomponent.Builder

@ProfileScope
@Subcomponent(modules = [ProfileModule::class, ProfileBindModule::class])
interface ProfileComponent {

    fun inject(profileFragment: ProfileFragment)

    @Builder
    interface ProfileBuilder {
        fun buildProfile(): ProfileComponent
    }
}