package com.slide.test.users.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.slide.test.core_ui.component.LoadingWheel
import com.slide.test.core_ui.component.TopAppBar
import com.slide.test.users.R.string

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserDetailsRoute(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { navigateBack() }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                "User details",
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(string.back)
                        )
                    }
                })
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
            UserDetails(state.value as UserDetailsViewState.Success)
        }

        is UserDetailsViewState.Error -> Error(state.value as UserDetailsViewState.Error)
    }

}

@Composable
fun UserDetails(
    state: UserDetailsViewState.Success,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(16.dp),
        text = state.toString()
    )
}

@Composable
private fun Loading(modifier: Modifier) {
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