package app.shiftlog.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = ShiftBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = ShiftGreen,
    error = ShiftRed,
    surface = SurfaceLight,
    background = SurfaceLight
)

private val DarkColors = darkColorScheme(
    primary = ShiftBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = ShiftGreen,
    error = ShiftRed,
    surface = SurfaceDark,
    background = SurfaceDark
)

@Composable
fun ShiftLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}