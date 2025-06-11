/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slide.test.exercise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.slide.test.core_ui.component.AppBackground
import com.slide.test.core_ui.theme.SliideTestTheme
import com.slide.test.exercise.navigation.SliideNavHost
import com.slide.test.users.navigation.UsersDestination

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SliideAppContainer() {
    SliideTestTheme {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = remember(navController) {
            navBackStackEntry?.destination?.route ?: UsersDestination.route
        }

        AppBackground {
            Scaffold(
                topBar = {
                    TopAppBar(
                        "Users",
                        Modifier.windowInsetsPadding(
                            WindowInsets.statusBars
                        )
                    )
                }
            ) { padding ->
                Row(
                    Modifier.fillMaxSize()
                        .padding(padding)
                        .consumedWindowInsets(padding)
                ) {
                    SliideNavHost(
                        navController = navController,
                        startDestination = currentDestination,
                    )

                }
            }
        }
    }
}

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    Box(
        Modifier
            .wrapContentSize()
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 4.dp,
                ambientColor = Color.Transparent,
                spotColor = MaterialTheme.colorScheme.onBackground
            )
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
            colors = colors,
            modifier = modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    SliideTestTheme {
        TopAppBar(title = "Users")
    }
}

