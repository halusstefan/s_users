package com.slide.test.core_ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    navigationIcon: @Composable () -> Unit = {}
) {
    Box(
        Modifier
            .windowInsetsPadding(
                WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
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
            navigationIcon = navigationIcon,
        )
    }
}