package com.example.mymoney.presentation.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2AE881),              // светло-зелёный как в светлой теме
    onPrimary = Color.Black,                  // хороший контраст в тёмной теме
    primaryContainer = Color(0xFF006D3A),     // более тёмный вариант зелёного
    onPrimaryContainer = Color(0xFF95F7BA),   // светлый текст на контейнере

    secondary = Color(0xFF42A685),            // адаптация светлой secondary
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF005234),
    onSecondaryContainer = Color(0xFFA2F6D5),

    tertiary = Pink40,                        // можно оставить как есть
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF5A003E),
    onTertiaryContainer = Color(0xFFFFD8EC),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1D1B20),
    onSurface = Color(0xFFE6E1E5),

    surfaceContainer = Color(0xFF2A282F),
    surfaceContainerHigh = Color(0xFF36343B),
    surfaceContainerHighest = Color(0xFF3E3C42),

    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    error = Color(0xFFE46962),
    onError = Color.Black,
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFFFDAD6)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2AE881),
    secondary = Color(0xFFD4FAE6),
    tertiary = Pink40,

    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFEF7FF),
    outlineVariant = Color(0xFFCAC4D0),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Green,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceContainer = Color(0xFFF3EDF7),
    secondaryContainer = Color(0xFFD4FAE6),
    surfaceContainerHigh = Color(0xFFECE6F0),
    surfaceContainerHighest = Color(0xFFE6E0E9),
    outline = Color(0xFF79747E),
    error = Color(0xFFE46962),
)

@Composable
fun MyMoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}