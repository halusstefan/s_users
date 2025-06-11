package com.slide.test.users.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slide.test.core.Result
import com.slide.test.repository.UsersRepository
import com.slide.test.repository.model.PostModel
import com.slide.test.users.navigation.UserDetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Stefan Halus on 11 Jun 2025
 */
@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userRepository: UsersRepository,
) : ViewModel() {

    val userId: Long = checkNotNull(
        savedStateHandle[UserDetailsDestination.Input.userIdArg]
    )

    val viewState: StateFlow<UserDetailsViewState> = userRepository.getUserPosts(userId)
        .map { postsResult -> postsResult.toUserDetailsViewState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserDetailsViewState.Loading,
        )
}

private fun Result<List<PostModel>>.toUserDetailsViewState(): UserDetailsViewState {
    return when (this) {
        is Result.Error -> UserDetailsViewState.Error(this.throwable?.localizedMessage ?: "")
        Result.Loading -> UserDetailsViewState.Loading
        is Result.Success<*> -> UserDetailsViewState.Success(
            userName = "Fake user name",
            userImage = UserAvatar.UserInitials("UN"),
            post = (this.data as List<PostModel>).firstOrNull()?.toPostUI()
        )
    }
}
