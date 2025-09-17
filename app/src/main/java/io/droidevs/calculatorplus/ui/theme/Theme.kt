package io.droidevs.calculatorplus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.droidevs.wallpaper.ui.theme.md_theme_dark_background
import io.droidevs.wallpaper.ui.theme.md_theme_dark_error
import io.droidevs.wallpaper.ui.theme.md_theme_dark_errorContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_inverseOnSurface
import io.droidevs.wallpaper.ui.theme.md_theme_dark_inversePrimary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_inverseSurface
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onBackground
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onError
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onErrorContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onPrimary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onPrimaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onSecondary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onSecondaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onSurface
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onSurfaceVariant
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onTertiary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_onTertiaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_outline
import io.droidevs.wallpaper.ui.theme.md_theme_dark_primary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_primaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_secondary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_secondaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_dark_surface
import io.droidevs.wallpaper.ui.theme.md_theme_dark_surfaceVariant
import io.droidevs.wallpaper.ui.theme.md_theme_dark_tertiary
import io.droidevs.wallpaper.ui.theme.md_theme_dark_tertiaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_background
import io.droidevs.wallpaper.ui.theme.md_theme_light_error
import io.droidevs.wallpaper.ui.theme.md_theme_light_errorContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_inverseOnSurface
import io.droidevs.wallpaper.ui.theme.md_theme_light_inversePrimary
import io.droidevs.wallpaper.ui.theme.md_theme_light_inverseSurface
import io.droidevs.wallpaper.ui.theme.md_theme_light_onBackground
import io.droidevs.wallpaper.ui.theme.md_theme_light_onError
import io.droidevs.wallpaper.ui.theme.md_theme_light_onErrorContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_onPrimary
import io.droidevs.wallpaper.ui.theme.md_theme_light_onPrimaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_onSecondary
import io.droidevs.wallpaper.ui.theme.md_theme_light_onSecondaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_onSurface
import io.droidevs.wallpaper.ui.theme.md_theme_light_onSurfaceVariant
import io.droidevs.wallpaper.ui.theme.md_theme_light_onTertiary
import io.droidevs.wallpaper.ui.theme.md_theme_light_onTertiaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_outline
import io.droidevs.wallpaper.ui.theme.md_theme_light_primary
import io.droidevs.wallpaper.ui.theme.md_theme_light_primaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_secondary
import io.droidevs.wallpaper.ui.theme.md_theme_light_secondaryContainer
import io.droidevs.wallpaper.ui.theme.md_theme_light_surface
import io.droidevs.wallpaper.ui.theme.md_theme_light_surfaceVariant
import io.droidevs.wallpaper.ui.theme.md_theme_light_tertiary
import io.droidevs.wallpaper.ui.theme.md_theme_light_tertiaryContainer

private val DarkColorScheme = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
)

private val LightColorScheme = lightColorScheme(

    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
)

/**
 * WallPaperTheme is the main theme composable for the app.
 * It determines which color scheme to apply based on system theme, dynamic color availability,
 * and user preferences.
 *
 * @param darkTheme Whether the app should use the dark theme. Defaults to the system setting.
 * @param dynamicColor Whether to enable dynamic colors (available on Android 12+).
 * @param content The composable content that will use the selected theme.
 */
@Composable
fun SimpleCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Automatically detects system dark mode.
    dynamicColor: Boolean = true, // Enables dynamic color if supported and requested.
    content: @Composable () -> Unit // Content composable that uses this theme.
) {
    // Call rememberColorScheme to calculate and cache the correct color scheme.
    val colorScheme = rememberColorScheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    )


    // Apply the calculated color scheme and typography to MaterialTheme.
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Typography is another theme parameter, assumed to be defined elsewhere.
        content = content // Render the given composable content within this theme.
    )
}

/**
 * rememberColorScheme determines and remembers the appropriate ColorScheme based on the given parameters.
 * It leverages the `remember` API to cache the computed value and avoid unnecessary recomposition.
 *
 * @param darkTheme Whether the app should use the dark theme.
 * @param dynamicColor Whether to enable dynamic colors (available on Android 12+).
 * @return The selected ColorScheme to be used in the app's theme.
 */
@Composable
fun rememberColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
): ColorScheme {
    // Obtain the current Android context, necessary for dynamic color APIs.
    val context = LocalContext.current

    // Use `remember` to cache the color scheme. It recalculates only when relevant inputs change:
    // - `context`: Needed for dynamic color APIs and reflects the app's environment.
    // - `darkTheme`: Determines whether to use dark or light colors.
    // - `dynamicColor`: Indicates whether to use Android's dynamic theming system.
    return remember(
        context, // Dependency on context, which might change during configuration changes.
        darkTheme, // Dependency on the selected theme mode.
        dynamicColor // Dependency on whether dynamic colors are enabled.
    ) {
        // Determine the color scheme based on the parameters.
        when {
            // Use dynamic color if available and requested.
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) {
                    dynamicDarkColorScheme(context) // Dynamic dark color scheme.
                } else {
                    dynamicLightColorScheme(context) // Dynamic light color scheme.
                }
            }
            // Fallback to predefined dark color scheme.
            darkTheme -> DarkColorScheme
            // Fallback to predefined light color scheme.
            else -> LightColorScheme
        }
    }
}
