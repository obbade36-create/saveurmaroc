package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TerracottaLight,
    onPrimary = Color.White,
    primaryContainer = TerracottaDark,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = SaffronGolden,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF623E00),
    onSecondaryContainer = SaffronLight,
    tertiary = MintLight,
    onTertiary = Color(0xFF003732),
    background = DarkEspresso,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECE5),
    onPrimaryContainer = TerracottaDark,
    secondary = SaffronAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = MintEmerald,
    onTertiary = Color.White,
    background = WarmSand,
    onBackground = TextPrimaryLight,
    surface = WarmSandSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF3E9DF),
    onSurfaceVariant = TextSecondaryLight,
    outline = WarmSandBorder
)

@Composable
fun SaveursDuMarocTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
