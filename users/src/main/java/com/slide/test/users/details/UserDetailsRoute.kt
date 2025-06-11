package com.slide.test.users.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserDetailsRoute(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { navigateBack() }

    Surface(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.tertiary)
    ) {
        Text("user details")
    }
}