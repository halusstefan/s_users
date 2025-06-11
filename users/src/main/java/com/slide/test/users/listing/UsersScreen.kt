package com.slide.test.users.listing

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import com.slide.test.core_ui.component.InfoDialog
import com.slide.test.core_ui.component.LoadingWheel
import com.slide.test.core_ui.component.TopAppBar
import com.slide.test.core_ui.theme.SliideTestTheme
import com.slide.test.usecase.users.model.Gender
import com.slide.test.usecase.users.model.UserStatus
import com.slide.test.users.R.string
import com.slide.test.users.model.UserUI

/**
 * Created by Stefan Halus on 18 May 2022
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UsersRoute(
    modifier: Modifier = Modifier,
    userDeleteResult: MutableLiveData<Boolean> = MutableLiveData(),
    viewModel: UsersViewModel = hiltViewModel(),
    navigateToDelete: (Long, String) -> Unit = { _, _ -> },
    navigateToCreate: () -> Unit = {},
    navigateToDetails: (Long) -> Unit = {},
) {
    val viewState by viewModel.observableState.subscribeAsState(viewModel.initialState)

    val userDeleted by userDeleteResult.observeAsState()

    LaunchedEffect(userDeleted) {
        if (userDeleted == true) {
            viewModel.dispatch(Action.LoadUserList)
            userDeleteResult.value = null
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar("Users")
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                    )
                    .padding(16.dp),
                onClick = navigateToCreate
            ) {
                Icon(
                    imageVector = Icons.TwoTone.PersonAdd,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    contentDescription = null,
                    modifier = Modifier
                )
            }
        }) { padding ->

        UsersScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            viewState = viewState,
            onDeleteIntent = { userUI -> navigateToDelete(userUI.id, userUI.name) },
            onUserClick = { userUI -> navigateToDetails(userUI.id) },
            onErrorAcknowledged = { viewModel.dispatch(Action.LoadUserList) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    viewState: State,
    onDeleteIntent: (UserUI) -> Unit = {},
    onUserClick: (UserUI) -> Unit = {},
    onErrorAcknowledged: () -> Unit = {}
) {
    when {
        viewState.isLoading -> LoadingUsers(modifier)
        viewState.isEmpty -> EmptyUsers(modifier)
        viewState.errorMessage != null -> ErrorDialog(
            errorMessage = viewState.errorMessage, onErrorAcknowledged
        )

        else -> UserList(
            modifier, viewState.userList,
            onDeleteIntent = onDeleteIntent,
            onUserClick = onUserClick,
        )
    }
}


@Composable
fun UserList(
    modifier: Modifier,
    userList: List<UserUI>,
    onDeleteIntent: (UserUI) -> Unit,
    onUserClick: (UserUI) -> Unit
) {
    val configuration = LocalConfiguration.current
    val columns = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 1
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns), modifier = modifier.padding(horizontal = 16.dp)

    ) {
        userList.forEach { userUI ->
            item {
                UserItem(
                    userUI = userUI,
                    onClick = onUserClick,
                    onLongTap = onDeleteIntent,
                )
            }
        }

        item {
            Spacer(
                Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
                )
            )
        }
    }
}

@Composable
fun LoadingUsers(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        LoadingWheel(
            contentDesc = stringResource(id = string.users_loading),
        )
    }
}

@Composable
fun EmptyUsers(modifier: Modifier) {
    Text(text = "Empty users", fontSize = 20.sp, modifier = modifier.padding(30.dp))
}

@Composable
fun ErrorDialog(
    errorMessage: String?, onDismiss: () -> Unit
) {
    InfoDialog(
        title = "Error",
        text = errorMessage ?: "",
        buttonText = "Retry",
        onApprove = onDismiss,
        onDismiss = onDismiss
    )

}


@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    val userList = listOf(
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.MALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
    )
    SliideTestTheme {
        UsersScreen(modifier = Modifier, viewState = State(userList = userList), { }, {}, {})
    }
}

@Preview(
    showBackground = true, device = Devices.TABLET, widthDp = 720, heightDp = 360
)
@Composable
fun UsersScreenLandScapePreview() {
    val userList = listOf(
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.MALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
        UserUI(
            10,
            "name",
            "name@email.com",
            Gender.FEMALE,
            UserStatus.INACTIVE,
            creationTime = "00:00:02"
        ),
    )
    SliideTestTheme {
        UsersScreen(modifier = Modifier, viewState = State(userList = userList), { }, { }, {})
    }
}