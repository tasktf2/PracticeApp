package com.setjy.practiceapp.domain.model

import com.setjy.practiceapp.domain.base.DomainMapper
import com.setjy.practiceapp.domain.base.Model
import com.setjy.practiceapp.presentation.ui.profile.UserItemUI
import com.setjy.practiceapp.presentation.ui.profile.UserStatus
import javax.inject.Inject

data class UserDomain(
    val userId: Int,
    val fullName: String,
    val avatarUrl: String,
    val userEmail: String,
    val userStatus: UserStatus
) : Model

class UserMapper @Inject constructor() : DomainMapper<UserDomain, UserItemUI> {
    override fun mapToPresentation(model: UserDomain): UserItemUI =
        UserItemUI(
            userId = model.userId,
            avatarUrl = model.avatarUrl,
            fullName = model.fullName,
            userEmail = model.userEmail,
            status = model.userStatus
        )
}