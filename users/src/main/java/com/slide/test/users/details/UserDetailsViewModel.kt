package com.slide.test.users.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slide.test.core.Result
import com.slide.test.repository.UsersRepository
import com.slide.test.repository.model.PostModel
import com.slide.test.repository.model.UserModel
import com.slide.test.users.common.UserAvatarFactory
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
    private val userAvatarFactory: UserAvatarFactory,
) : ViewModel() {

    val userId: Long = checkNotNull(
        savedStateHandle[UserDetailsDestination.Input.userIdArg]
    )
    val viewState = userRepository.getUserPost(userId)
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
        result: Result<PostModel?>,
        user: UserModel?
    ): UserDetailsViewState {
        if (user == null) return UserDetailsViewState.Error("User not found")

        return UserDetailsViewState.Success(
            userName = user.name,
            userImage = userAvatarFactory.create(user.id, user.name),
            userEmail = user.email,
            postViewState = result.toPostViewState(),
        )
    }

}

private fun Result<PostModel?>.toPostViewState(): PostViewState {
    return when (this) {
        is Result.Error -> PostViewState.Error(this.throwable?.localizedMessage ?: "")
        Result.Loading -> PostViewState.Loading
        is Result.Success<*> -> PostViewState.Success(
            post = (this.data as PostModel?).toPostUI()
        )
    }
}
