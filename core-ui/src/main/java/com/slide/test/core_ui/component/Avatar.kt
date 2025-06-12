package com.slide.test.core_ui.component

/**
 * Created by Stefan Halus on 12 Jun 2025
 */
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.slide.test.core_ui.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RemoteAvatar(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    GlideImage(
        model = imageUrl,
        contentDescription = stringResource(id = R.string.user_profile_content_description),
        modifier = modifier.circularAvatarModifier()
    ) {
        it.centerCrop()
    }
}

@Composable
fun InitialsAvatar(
    initials: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .circularAvatarModifier()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun Modifier.circularAvatarModifier(): Modifier = this
    .size(46.dp)
    .clip(CircleShape)


@Preview(showBackground = true)
@Composable
fun InitialsAvatarPreview() {
    InitialsAvatar("SH")
}