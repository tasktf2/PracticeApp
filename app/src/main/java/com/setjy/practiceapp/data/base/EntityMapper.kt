package com.setjy.practiceapp.data.base

import com.setjy.practiceapp.domain.base.Model

interface EntityMapper<ME : ModelEntity, M : Model> {
    fun mapToDomain(entity: ME): M

    fun mapToEntity(model: M): ME
}