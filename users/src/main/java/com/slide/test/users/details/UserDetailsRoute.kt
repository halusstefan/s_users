package com.slide.test.users.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.slide.test.core_ui.component.LoadingWheel
import com.slide.test.core_ui.component.TopAppBar
import com.slide.test.users.R.string
import com.slide.test.users.common.UserAvatar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserDetailsRoute(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { navigateBack() }
    Scaffold(
        modifier = modifier.background(Color.White),
        topBar = {
            TopAppBar(
                stringResource(string.user_details_title),
                navigateBack = navigateBack,
            )
        }) { padding ->

        UserDetailsScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
        )
    }
}

@Composable
fun UserDetailsScreen(
    userDetailsViewModel: UserDetailsViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val state = userDetailsViewModel.viewState.collectAsState()

    when (state.value) {
        is UserDetailsViewState.Loading -> Loading(modifier)
        is UserDetailsViewState.Success -> {
            UserDetails(modifier = modifier, state = state.value as UserDetailsViewState.Success)
        }

        is UserDetailsViewState.Error -> Error(state.value as UserDetailsViewState.Error)
    }

}

@Composable
fun UserDetails(
    state: UserDetailsViewState.Success,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        UserAvatar(
            state.userImage,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = state.userName,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            modifier = Modifier
                .padding(top = 10.dp),
            text = state.userEmail,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))
        PostArea(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(24.dp),
            postViewState = state.postViewState,
        )
    }
}

@Composable
fun PostArea(
    postViewState: PostViewState,
    modifier: Modifier = Modifier
) {
    when (postViewState) {
        is PostViewState.Error -> Text(
            "Loading post failed",
            modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        PostViewState.Loading -> Loading(modifier)
        is PostViewState.Success -> PostSuccess(postViewState.post, modifier)
    }
}

@Composable
fun PostSuccess(
    post: PostUI?,
    modifier: Modifier = Modifier,
) {
    if (post == null) {
        Text(
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "No posts available"
        )
    } else {
        Column(
            modifier = modifier,
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        LoadingWheel(
            contentDesc = stringResource(id = string.loading_user_posts),
        )
        Text(stringResource(id = string.loading_user_posts))
    }
}

@Composable
private fun Error(
    state: UserDetailsViewState.Error,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.fillMaxSize(),
        text = state.message,
    )
}

@Preview
@Composable
fun UserDetailsPreview() {
    UserDetails(
        UserDetailsViewState.Success(
            userName = "Stefan Halus",
            userImage = UserAvatar.UserInitials("DS"),
            userEmail = "james.wilson@example-pet-store.com",
            postViewState = PostViewState.Success(
                post = PostUI(
                    title = "Title",
                    body = "Body"
                ),
            )
        )
    )
}

@Preview
@Composable
fun UserDetailsLoadingPreview() {
    UserDetails(
        UserDetailsViewState.Success(
            userName = "Stefan Halus",
            userImage = UserAvatar.UserInitials("DS"),
            userEmail = "james.wilson@example-pet-store.com",
            postViewState = PostViewState.Loading,
        )
    )
}

@Preview
@Composable
fun UserDetailsErrorPreview() {
    UserDetails(
        UserDetailsViewState.Success(
            userName = "Stefan Halus",
            userImage = UserAvatar.UserInitials("DS"),
            userEmail = "james.wilson@example-pet-store.com",
            postViewState = PostViewState.Error("Loading error"),
        )
    )
}


@Preview
@Composable
fun UserDetailsNoPostPreview() {
    UserDetails(
        UserDetailsViewState.Success(
            userName = "Stefan Halus",
            userImage = UserAvatar.UserInitials("DS"),
            userEmail = "james.wilson@example-pet-store.com",
            postViewState = PostViewState.Success(null),
        )
    )
}