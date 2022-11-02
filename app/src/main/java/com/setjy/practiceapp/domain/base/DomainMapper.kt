package com.setjy.practiceapp.domain.base

import com.setjy.practiceapp.presentation.base.Item


interface DomainMapper<MP : Item, M : Model> {
    fun mapToPresentation(model: M): MP

//    fun mapToDomain(presentation: MP): M todo нужен ли он?
}