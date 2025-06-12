package com.slide.test.core_ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

/**
 * Light default theme color scheme
 */

val LightColorScheme = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = light_backgroundTertiaryAvatar,
    onSecondaryContainer = md_theme_light_onPrimary,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,

    background = light_backgroundPrimary,              // Your backgroundPrimary
    onBackground = light_textPrimary,                  // Your textPrimary

    surface = light_backgroundPrimary,                 // Your backgroundPrimary for main surfaces
    onSurface = light_textPrimary,                     // Your textPrimary on main surfaces

    surfaceVariant = light_backgroundSecondary,        // Your backgroundSecondary for variant surfaces
    onSurfaceVariant = light_textSecondary,            // Your textSecondary for variant surfaces

    outline = light_textSecondary, // Often a muted text color or border
)

val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = dark_secondaryContainer, // Using adapted avatar bg
    onSecondaryContainer = dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onErrorContainer = md_theme_dark_onErrorContainer,

    background = dark_background,
    onBackground = dark_onBackground,

    surface = dark_surface,
    onSurface = dark_onSurface,

    surfaceVariant = dark_surfaceVariant,
    onSurfaceVariant = dark_onSurfaceVariant, // Your light_textEmail could be adapted here too

    outline = dark_onSurfaceVariant, // Often a muted text color or border
)

@Composable
fun SliideTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}