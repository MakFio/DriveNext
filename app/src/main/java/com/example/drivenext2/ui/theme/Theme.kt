package com.example.drivenext2.ui.theme

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Цветовая схема для тёмной темы
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // Основной цвет (фиолетовый)
    onPrimary = Color.White, // Цвет текста на основном цвете
    background = Color(0xFF121212), // Цвет фона
    onBackground = Color.White, // Цвет текста на фоне
    surface = Color(0xFF1C1B1F), // Цвет поверхности
    onSurface = Color.White // Цвет текста на поверхности
)

// Цветовая схема для светлой темы
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Основной цвет (фиолетовый)
    onPrimary = Color.White, // Цвет текста на основном цвете
    background = Color.White, // Цвет фона
    onBackground = Color.Black, // Цвет текста на фоне
    surface = Color.White, // Цвет поверхности
    onSurface = Color.Black // Цвет текста на поверхности
)

@Composable
fun DriveNext2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
            val activity = view.context as Activity
            activity.window.statusBarColor = Color.Transparent.hashCode() // Прозрачный статус-бар
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}