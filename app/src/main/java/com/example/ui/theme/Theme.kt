package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = AccentSky,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = Color(0xFF0369A1),
  secondary = GemAmberPrimary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFEF3C7),
  onSecondaryContainer = Color(0xFF92400E),
  tertiary = GemEmeraldPrimary,
  onTertiary = Color.White,
  background = BgCanvasLight,
  onBackground = InkDark,
  surface = Color.White,
  onSurface = InkDark,
  surfaceVariant = Color(0xFFF5EFE6),
  onSurfaceVariant = InkMuted,
  outline = Color(0xFFD6C7B2),
)

private val DarkColorScheme = darkColorScheme(
  primary = AccentSkyLight,
  onPrimary = Color(0xFF082F49),
  primaryContainer = Color(0xFF075985),
  onPrimaryContainer = Color(0xFFE0F2FE),
  secondary = GemAmberLight,
  onSecondary = Color(0xFF451A03),
  secondaryContainer = Color(0xFF78350F),
  onSecondaryContainer = Color(0xFFFEF3C7),
  tertiary = GemEmeraldLight,
  onTertiary = Color(0xFF022C22),
  background = Color(0xFF1C1713),
  onBackground = Color(0xFFF3ECE4),
  surface = Color(0xFF28211C),
  onSurface = Color(0xFFF3ECE4),
  surfaceVariant = Color(0xFF382E27),
  onSurfaceVariant = Color(0xFFD4C7BC),
  outline = Color(0xFF5A4D43),
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent warm branding
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
