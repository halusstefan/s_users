package com.slide.test.usecase.users.model

import com.slide.test.repository.model.UserModel

/**
 * Created by Stefan Halus on 19 May 2022
 */
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val status: UserStatus,
    val creationTime: Long
)

fun UserModel.toUseCase(creationTime: Long): User {
    return User(
        id = id,
        name = name,
        email = email,
        status = status.toUseCase(),
        creationTime = creationTime
    )
}
