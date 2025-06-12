package com.slide.test.users.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slide.test.core.Result
import com.slide.test.repository.UsersRepository
import com.slide.test.repository.model.PostModel
import com.slide.test.repository.model.UserModel
import com.slide.test.usecase.users.model.User
import com.slide.test.users.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Stefan Halus on 11 Jun 2025
 */
@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UsersRepository,
) : ViewModel() {

    val userId: Long = checkNotNull(
        savedStateHandle[UserDetailsDestination.Input.userIdArg]
    )
    val viewState = userRepository.getUserPosts(userId)
        .combine(getUserDetailsFlow()) { postsResult, userDetails ->
            createUserDetailsViewState(postsResult, userDetails)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserDetailsViewState.Loading,
        )

    private fun getUserDetailsFlow(): Flow<UserModel?> = flow {
        emit(userRepository.getUser(userId))
    }

    private fun createUserDetailsViewState(
        result: Result<List<PostModel>>,
        user: UserModel?
    ): UserDetailsViewState {
        if (user == null) return UserDetailsViewState.Error("User not found")

        return UserDetailsViewState.Success(
            userName = user.name,
            userImage = getUserAvatar(user),
            userEmail = user.email,
            postViewState = result.toPostViewState(),
        )
    }

    private fun getUserAvatar(user: UserModel): UserAvatar {
        if(user.id %2 == 0L) {
            return UserAvatar.UserInitials(generateInitials(user.name))
        } else {
            return UserAvatar.UserImage("https://picsum.photos/200/200")
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

private fun Result<List<PostModel>>.toPostViewState(): PostViewState {
    return when (this) {
        is Result.Error -> PostViewState.Error(this.throwable?.localizedMessage ?: "")
        Result.Loading -> PostViewState.Loading
        is Result.Success<*> -> PostViewState.Success(
            post = (this.data as List<PostModel>).firstOrNull()?.toPostUI()
        )
    }
}
