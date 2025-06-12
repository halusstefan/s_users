package com.slide.test.users.common

import com.slide.test.repository.model.UserModel
import com.slide.test.users.details.UserAvatar
import javax.inject.Inject

/**
 * Created by Stefan Halus on 12 Jun 2025
 */

interface UserAvatarFactory {
    fun create(user: UserModel): UserAvatar
}

class UserAvatarFactoryImplementation @Inject constructor() : UserAvatarFactory {
    override fun create(user: UserModel): UserAvatar {
        return if (user.id % 2 == 0L) {
            UserAvatar.UserInitials(generateInitials(user.name))
        } else {
            UserAvatar.UserImage("https://picsum.photos/200/200")
        }
    }

    private fun generateInitials(name: String): String {
        if (name.isBlank()) return ""
        val parts = name.split(" ").filter { it.isNotBlank() }
        return when {
            parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}"
            parts.isNotEmpty() -> "${parts[0].first()}"
            else -> ""
        }.uppercase()
    }
}