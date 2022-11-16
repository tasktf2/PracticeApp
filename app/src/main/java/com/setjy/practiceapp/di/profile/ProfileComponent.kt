package com.setjy.practiceapp.di.profile

import com.setjy.practiceapp.di.AppComponent
import com.setjy.practiceapp.presentation.ui.profile.ProfileFragment
import dagger.Component

@ProfileScope
@Component(dependencies = [AppComponent::class], modules = [ProfileModule::class])
interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)
}