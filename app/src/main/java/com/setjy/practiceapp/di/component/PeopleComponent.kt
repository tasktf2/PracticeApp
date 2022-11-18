package com.setjy.practiceapp.di.component

import com.setjy.practiceapp.di.module.people.PeopleBindModule
import com.setjy.practiceapp.di.module.people.PeopleModule
import com.setjy.practiceapp.di.scope.PeopleScope
import com.setjy.practiceapp.presentation.ui.people.PeopleFragment
import dagger.Subcomponent
import dagger.Subcomponent.Builder

@PeopleScope
@Subcomponent(modules = [PeopleModule::class, PeopleBindModule::class])
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)

    @Builder
    interface PeopleBuilder {
        fun buildPeople(): PeopleComponent
    }
}