package com.example.drivenext2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // Пример цвета для тёмной темы
    secondary = Color(0xFF03DAC5),
    background = Color(0xFF121212), // Темно-серый фон
    surface = Color(0xFF1C1C1C),   // Сероватый фон для карточек и поверхностей
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Пример цвета для светлой темы
    secondary = Color(0xFF03DAC5),
    background = Color.White,   // Белый фон
    surface = Color.White,      // Белый фон для карточек и поверхностей
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun DriveNext2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}