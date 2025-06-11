package com.slide.test.users.details

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slide.test.core_ui.component.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
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
                            imageVector = Icons.Filled.ArrowBack, // Or Icons.AutoMirrored.Filled.ArrowBack
                            contentDescription = "Back" // Important for accessibility
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
fun UserDetailsScreen(modifier: Modifier) {
    Text(text = "User Details", modifier = modifier.padding(30.dp))
}