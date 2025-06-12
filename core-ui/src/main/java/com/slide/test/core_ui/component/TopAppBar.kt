package com.slide.test.core_ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.slide.test.core_ui.R

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigateBack: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            )
            .fillMaxWidth()
            .height(54.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        navigateBack?.let {
            Box(
                modifier = modifier
                    .clickable { navigateBack() }
                    .fillMaxHeight()
                    .padding(start = 8.dp, end = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .width(12.dp)
                        .height(20.5.dp)
                        .rotate(180f)
                )
            }
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.padding(start = if (navigateBack != null) 0.dp else 24.dp),
        )
    }
}
