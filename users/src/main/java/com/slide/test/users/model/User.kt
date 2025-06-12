package com.slide.test.users.model

import com.slide.test.core.TimeFormatter
import com.slide.test.usecase.users.model.Gender
import com.slide.test.usecase.users.model.User
import com.slide.test.usecase.users.model.UserStatus
import com.slide.test.users.common.UserAvatarFactory
import com.slide.test.users.details.UserAvatar

/**
 * Created by Stefan Halus on 19 May 2022
 */
data class UserUI(
    val id: Long,
    val name: String,
    val email: String,
    val avatar: UserAvatar,
    val status: UserStatus,
    val creationTime: String
)

fun User.toUI(timeFormatter: TimeFormatter, userAvatarFactory: UserAvatarFactory): UserUI {
    return UserUI(
        id = id,
        name = name,
        email = email,
        avatar = userAvatarFactory.create(id, name),
        status = status,
        creationTime = timeFormatter.formatDuration(creationTime)
    )
}