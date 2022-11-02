package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.model.UserItemUI
import com.setjy.practiceapp.presentation.model.UserStatus

data class UserDomain(
    val userId: Int,
    val fullName: String,
    val avatarUrl: String,
    val userEmail: String,
    val userStatus: UserStatus
) : Model()

class UserMapper : DomainMapper<UserItemUI, UserDomain> {
    override fun mapToPresentation(model: UserDomain): UserItemUI =
        UserItemUI(
            userId = model.userId,
            avatarUrl = model.avatarUrl,
            fullName = model.fullName,
            userEmail = model.userEmail,
            status = model.userStatus
        )
}