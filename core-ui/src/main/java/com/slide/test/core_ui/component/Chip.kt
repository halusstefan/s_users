package com.slide.test.core_ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Created by Stefan Halus on 22 May 2022
 */
@Composable
fun CustomChip(
    checked: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onCheckedChanged: (Boolean) -> Unit
) {
    // define properties to the chip
    // such as color, shape, width
    Surface(
        color = when {
            checked -> MaterialTheme.colorScheme.onSurface
            else -> Color.Transparent
        },
        contentColor = when {
            checked -> MaterialTheme.colorScheme.onPrimary
            else -> Color.LightGray
        },
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = when {
                checked -> MaterialTheme.colorScheme.primary
                else -> Color.LightGray
            }
        ),
        modifier = modifier.clickable { onCheckedChanged(!checked) }
    ) {
        // Add text to show the data that we passed
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )

    }
}