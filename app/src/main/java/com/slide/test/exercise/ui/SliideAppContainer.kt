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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
                            "Users", Modifier.windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                            )
                        )
                }
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                ) {
                    SliideNavHost(
                        navController = navController,
                        startDestination = currentDestination,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding)
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

