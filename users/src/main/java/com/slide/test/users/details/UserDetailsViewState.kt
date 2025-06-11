package com.slide.test.users.details

import com.slide.test.repository.model.PostModel

/**
 * Created by Stefan Halus on 11 Jun 2025
 */

sealed class UserDetailsViewState {

    object Loading : UserDetailsViewState()
    data class Error(val message: String) : UserDetailsViewState()
    data class Success(
        val userName: String,
        val userImage: UserAvatar,
        val userEmail: String,
        val postViewState: PostViewState?,
    ) : UserDetailsViewState()
}

sealed class UserAvatar {
    data class UserInitials(val initials: String) : UserAvatar()
    data class UserImage(val url: String) : UserAvatar()
}

sealed class PostViewState {
    object Loading: PostViewState()
    data class Error(val message: String): PostViewState()
    data class Success(val post: PostUI?): PostViewState()
}

data class PostUI(
    val title: String,
    val body: String,
)

fun PostModel.toPostUI(): PostUI {
    return PostUI(
        title = title,
        body = body,
    )
}