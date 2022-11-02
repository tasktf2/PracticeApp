package com.setjy.practiceapp.data.base

import com.setjy.practiceapp.domain.base.Model

interface RemoteMapper<M : Model, MR : ModelRemote, ME : ModelEntity> {
    fun mapToDomain(remote: MR): M
    fun mapToEntity(remote: MR): ME
}