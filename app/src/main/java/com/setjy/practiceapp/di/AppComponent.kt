package com.setjy.practiceapp.di

import com.setjy.practiceapp.presentation.ui.profile.ProfileFragment
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(profileFragment: ProfileFragment)
}