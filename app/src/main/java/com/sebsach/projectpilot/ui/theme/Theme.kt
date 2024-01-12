package com.sebsach.projectpilot.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val customColorScheme = ColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1976D2),
    onPrimaryContainer = Color.Red,
    inversePrimary = Color.White,
    secondary = Color(0xFF3C3C3C),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFA000),
    onSecondaryContainer = Color.Black,
    tertiary = Color(0xFF4CAF50),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF388E3C),
    onTertiaryContainer = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF1F1F1),
    onSurfaceVariant = Color.Black,
    surfaceTint = Color(0x1A000000),
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFD32F2F),
    onErrorContainer = Color.White,
    outline = Color(0xFF9E9E9E),
    outlineVariant = Color(0xFF757575),
    scrim = Color.Red
)

@Composable
fun ProjectPilotTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = customColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.White.toArgb()
            window.navigationBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}