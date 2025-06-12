package com.slide.test.users.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slide.test.core_ui.component.InitialsAvatar
import com.slide.test.core_ui.component.RemoteAvatar
import com.slide.test.users.details.UserAvatar

/**
 * Created by Stefan Halus on 12 Jun 2025
 */
@Composable
fun UserAvatar(
    userAvatar: UserAvatar,
    modifier: Modifier = Modifier,
) {
    when (userAvatar) {
        is UserAvatar.UserImage -> RemoteAvatar(userAvatar.url, modifier)
        is UserAvatar.UserInitials -> InitialsAvatar(userAvatar.initials, modifier)
    }
}