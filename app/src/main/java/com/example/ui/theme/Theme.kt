package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TikTokColorScheme = darkColorScheme(
  primary = TikTokRed,
  onPrimary = TikTokWhite,
  secondary = TikTokCyan,
  onSecondary = TikTokBlack,
  tertiary = TikTokGold,
  background = TikTokBlack,
  onBackground = TikTokWhite,
  surface = TikTokDark,
  onSurface = TikTokWhite,
  surfaceVariant = TikTokSurface,
  onSurfaceVariant = TikTokLightGray
)

@Composable
fun TikTokTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = TikTokColorScheme,
    typography = Typography,
    content = content
  )
}

// Alias for backwards compatibility
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  TikTokTheme(content = content)
}
