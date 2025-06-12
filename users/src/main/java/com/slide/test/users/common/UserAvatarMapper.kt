package com.slide.test.users.common

import com.slide.test.repository.model.UserModel
import com.slide.test.users.details.UserAvatar
import javax.inject.Inject

/**
 * Created by Stefan Halus on 12 Jun 2025
 */

interface UserAvatarFactory {
    fun create(userId: Long, userName: String): UserAvatar
}

class UserAvatarFactoryImplementation @Inject constructor() : UserAvatarFactory {
    override fun create(userId: Long, userName: String): UserAvatar {
        return if (userId % 2 == 0L) {
            UserAvatar.UserInitials(generateInitials(userName))
        } else {
            val picId = userId % 1000
            UserAvatar.UserImage("https://picsum.photos/id/$picId/200/200")
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