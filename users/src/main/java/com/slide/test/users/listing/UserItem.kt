package com.slide.test.users.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Female
import androidx.compose.material.icons.twotone.Male
import androidx.compose.material.icons.twotone.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slide.test.core_ui.theme.SliideTestTheme
import com.slide.test.usecase.users.model.Gender
import com.slide.test.usecase.users.model.UserStatus
import com.slide.test.users.model.UserUI

/**
 * Created by Stefan Halus on 20 May 2022
 */
@Composable
fun UserItem(
    userUI: UserUI,
    onClick: () -> Unit,
    onLongTap: (UserUI) -> Unit,
    modifier: Modifier = Modifier,
    itemSeparation: Dp = 16.dp
) {
    val haptic = LocalHapticFeedback.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = itemSeparation)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongTap(userUI)
                    }
                )
            }
    ) {
        GenderIcon(userUI.gender)
        Spacer(modifier = Modifier.width(16.dp))
        UserContent(userUI)
    }
}

@Composable
private fun UserContent(userUI: UserUI, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = userUI.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                vertical = if (userUI.email.isEmpty()) 0.dp else 4.dp
            )
        )
        Text(
            text = userUI.email,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = userUI.status.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = userUI.creationTime,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun GenderIcon(gender: Gender, modifier: Modifier = Modifier) {
    val icon = when (gender) {
        Gender.FEMALE -> Icons.TwoTone.Female
        Gender.MALE -> Icons.TwoTone.Male
        Gender.UNKNOWN -> Icons.TwoTone.Minimize
    }

    Icon(
        imageVector = icon,
        tint = MaterialTheme.colorScheme.surfaceTint,
        contentDescription = null,
        modifier = modifier
            .size(64.dp)
            .padding(5.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primaryContainer)

    )
}

@Preview
@Composable
private fun UserCardPreview() {
    SliideTestTheme {
        Surface {
            UserItem(
                UserUI(10, "name", "name@email.com", Gender.MALE, UserStatus.INACTIVE, creationTime = "00:00:02"),
                onClick = { },
                onLongTap = { }
            )
        }
    }
}

@Preview
@Composable
private fun UserLongNameCardPreview() {
    SliideTestTheme {
        Surface {
            UserItem(
                UserUI(
                    10,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                    "name@email.com",
                    Gender.MALE,
                    UserStatus.INACTIVE,
                    creationTime = "00:00:02"
                ),
                onClick = { },
                onLongTap = { }
            )
        }
    }
}
