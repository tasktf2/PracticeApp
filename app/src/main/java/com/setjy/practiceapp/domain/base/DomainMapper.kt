package com.setjy.practiceapp.domain.base

import com.setjy.practiceapp.presentation.base.Item


interface DomainMapper<in M : Model, out MP : Item> {
    fun mapToPresentation(model: M): MP
}