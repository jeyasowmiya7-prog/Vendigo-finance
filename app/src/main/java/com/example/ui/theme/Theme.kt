package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = RendeskButtonBlack,
    primaryContainer = Color(0xFF2C2430),
    onPrimaryContainer = RendeskPastelPink,
    secondary = RendeskSoftCoral,
    onSecondary = RendeskButtonBlack,
    secondaryContainer = Color(0xFF2E243B),
    onSecondaryContainer = RendeskLavender,
    tertiary = RendeskLavender,
    onTertiary = RendeskButtonBlack,
    background = Color(0xFF141316),
    surface = Color(0xFF1C1A1F),
    onBackground = Color(0xFFF5F4F7),
    onSurface = Color(0xFFF5F4F7),
    surfaceVariant = Color(0xFF28262E),
    onSurfaceVariant = Color(0xFFB5B2BC),
    outline = Color(0xFF45424D),
    error = RendeskSoftCoral,
    onError = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RendeskButtonBlack,               // Black #000000
    onPrimary = Color.White,
    primaryContainer = RendeskPastelPink,       // Pastel pink #F5C9D2
    onPrimaryContainer = RendeskPrimaryText,    // Near-black #151515
    secondary = RendeskSoftCoral,               // Soft coral #FF777F
    onSecondary = Color.White,
    secondaryContainer = RendeskLightPurpleTint,// #EEE7FA
    onSecondaryContainer = RendeskPrimaryText,
    tertiary = RendeskLavender,                 // Lavender #C9B9F2
    onTertiary = RendeskPrimaryText,
    tertiaryContainer = RendeskBlushPink,       // Blush pink #F4BFCB
    onTertiaryContainer = RendeskPrimaryText,
    background = RendeskPrimaryBg,              // Soft off-white #FAF8F8
    surface = RendeskCardWhite,                 // White #FFFFFF
    surfaceVariant = RendeskSecondaryBg,        // Light cool gray #E7E8EF
    onBackground = RendeskPrimaryText,          // Near-black #151515
    onSurface = RendeskPrimaryText,
    onSurfaceVariant = RendeskSecondaryText,    // Muted gray #77747A
    outline = RendeskBorderSoftGray,            // Soft gray #E8E5E8
    error = RendeskSoftCoral,
    onError = Color.White,
    errorContainer = FinanceProfitBg,
    onErrorContainer = RendeskPrimaryText
  )

@Composable
fun VendigoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
