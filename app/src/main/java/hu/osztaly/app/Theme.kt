package hu.osztaly.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RedCell = Color(0xFFE53935)
val GreenCell = Color(0xFF43A047)

private val LightColors = lightColorScheme(
    primary = Color(0xFFF57C00),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFF3E2000),
    background = Color(0xFFFFF8F0),
    onBackground = Color(0xFF2B2118),
    surface = Color(0xFFFFF8F0),
    onSurface = Color(0xFF2B2118),
    surfaceVariant = Color(0xFFFFE9D2),
    onSurfaceVariant = Color(0xFF4A3B2E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFA726),
    onPrimary = Color(0xFF3E2000),
    primaryContainer = Color(0xFF7A3E00),
    onPrimaryContainer = Color(0xFFFFE0B2),
    background = Color(0xFF161210),
    onBackground = Color(0xFFF3E9DF),
    surface = Color(0xFF161210),
    onSurface = Color(0xFFF3E9DF),
    surfaceVariant = Color(0xFF2B221C),
    onSurfaceVariant = Color(0xFFE0D2C4)
)

@Composable
fun OsztalyTheme(dark: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
