package com.setjy.practiceapp.data.base

import com.setjy.practiceapp.domain.base.Model

interface EntityMapper<M : Model, ME : ModelEntity> {
    fun mapToDomain(entity: ME): M

    fun mapToEntity(model: M): ME
}