package com.setjy.practiceapp.data.base

import com.setjy.practiceapp.domain.base.Model

interface RemoteMapper<in MR : ModelRemote, out M : Model, out ME : ModelEntity> {
    fun mapToDomain(remote: MR): M
    fun mapToEntity(remote: MR): ME
}