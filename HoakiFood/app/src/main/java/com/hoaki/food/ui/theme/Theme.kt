package com.hoaki.food.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextWhite,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = TextWhite,
    secondary = Success,
    onSecondary = TextWhite,
    secondaryContainer = SuccessDark,
    onSecondaryContainer = TextWhite,
    tertiary = AccentOrange,
    onTertiary = TextWhite,
    error = Error,
    onError = TextWhite,
    background = Color(0xFF1F2937),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF374151),
    onSurface = Color(0xFFE5E7EB),
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextWhite,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = PrimaryDark,
    secondary = Success,
    onSecondary = TextWhite,
    secondaryContainer = SuccessLight,
    onSecondaryContainer = SuccessDark,
    tertiary = AccentOrange,
    onTertiary = TextWhite,
    error = Error,
    onError = TextWhite,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Border,
    outlineVariant = Divider,
)

@Composable
fun HoakiFoodTheme(
    darkTheme: Boolean = false, // Always use light theme by default
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disable dynamic colors to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
