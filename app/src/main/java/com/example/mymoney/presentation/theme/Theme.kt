package com.example.mymoney.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mymoney.R


private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2AE881),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF006D3A),
    onPrimaryContainer = Color(0xFF95F7BA),
    secondary = Color(0xFF42A685),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF005234),
    onSecondaryContainer = Color(0xFFA2F6D5),
    tertiary = Pink40,
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
    onPrimaryContainer = greenLightPrimary,
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

private fun blueColorScheme() = lightColorScheme(
    primary = blueLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFF1565C0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF90CAF9),
    onSecondaryContainer = Color(0xFF0277BD),
    tertiary = Color(0xFF0288D1),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF81D4FA),
    onTertiaryContainer = Color(0xFF01579B),
    background = blueLightBackground,
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE3F2FD),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFF64B5F6),
    error = Color(0xFFE46962)
)

// Темная версия синей темы
private fun blueDarkColorScheme() = darkColorScheme(
    primary = blueDarkPrimary,
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFF90CAF9),
    onSecondary = Color(0xFF0277BD),
    secondaryContainer = Color(0xFF1976D2),
    onSecondaryContainer = Color(0xFF81D4FA),
    tertiary = Color(0xFF81D4FA),
    onTertiary = Color(0xFF01579B),
    tertiaryContainer = Color(0xFF0288D1),
    onTertiaryContainer = Color(0xFFB3E5FC),
    background = blueDarkBackground,
    onBackground = Color(0xFFE3F2FD),
    surface = Color(0xFF0D1B2A),
    onSurface = Color(0xFFE3F2FD),
    surfaceVariant = Color(0xFF1E3A5F),
    onSurfaceVariant = Color(0xFFB3E5FC),
    outline = Color(0xFF42A5F5),
    error = Color(0xFFE46962)
)

private fun greenLightColorScheme() = lightColorScheme(
    primary = Color(0xFF2AE881),
    secondary = Color(0xFFD4FAE6),
    tertiary = Pink40,

    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFEF7FF),
    outlineVariant = Color(0xFFCAC4D0),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = greenLightPrimary,
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

private fun greenDarkColorScheme() = darkColorScheme(
    primary = greenDarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF006D3A),
    onPrimaryContainer = Color(0xFF95F7BA),

    secondary = Color(0xFF42A685),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF005234),
    onSecondaryContainer = Color(0xFFA2F6D5),

    tertiary = Pink40,
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

private fun purpleColorScheme() = lightColorScheme(
    primary = purpleLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A148C),
    secondary = Color(0xFF9C27B0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCE93D8),
    onSecondaryContainer = Color(0xFF6A1B9A),
    tertiary = Color(0xFFAB47BC),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1C4E9),
    onTertiaryContainer = Color(0xFF512DA8),
    background = purpleLightBackground,
    onBackground = Color(0xFF1A1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFF3E5F5),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFF9C27B0),
    error = Color(0xFFE46962)
)

// Темная версия фиолетовой темы
private fun purpleDarkColorScheme() = darkColorScheme(
    primary = purpleDarkPrimary,
    onPrimary = Color(0xFF4A148C),
    primaryContainer = Color(0xFF6A1B9A),
    onPrimaryContainer = Color(0xFFE1BEE7),
    secondary = Color(0xFFBA68C8),
    onSecondary = Color(0xFF38006B),
    secondaryContainer = Color(0xFF7B1FA2),
    onSecondaryContainer = Color(0xFFE1BEE7),
    tertiary = Color(0xFFAB47BC),
    onTertiary = Color(0xFF4A148C),
    tertiaryContainer = Color(0xFF9C27B0),
    onTertiaryContainer = Color(0xFFD1C4E9),
    background = purpleDarkBackground,
    onBackground = Color(0xFFF3E5F5),
    surface = Color(0xFF2A1A2E),
    onSurface = Color(0xFFF3E5F5),
    surfaceVariant = Color(0xFF3F2A47),
    onSurfaceVariant = Color(0xFFE1BEE7),
    outline = Color(0xFFBA68C8),
    error = Color(0xFFE46962)
)

private fun orangeLightColorScheme() = lightColorScheme(
    primary = orangeLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFF3E0),
    onPrimaryContainer = Color(0xFFE65100),
    secondary = Color(0xFFFFA726),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFFBF360C),
    background = orangeLightBackground,
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20),
    surfaceContainer = Color(0xFFF3EDF7),
    outline = Color(0xFF79747E),
    error = Color(0xFFE46962)
)

private fun orangeDarkColorScheme() = darkColorScheme(
    primary = orangeDarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color(0xFFFFE0B2),
    secondary = Color(0xFFFFA726),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFBF360C),
    onSecondaryContainer = Color(0xFFFFE0B2),
    background = orangeDarkBackground,
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1D1B20),
    onSurface = Color(0xFFE6E1E5),
    surfaceContainer = Color(0xFF2A282F),
    outline = Color(0xFF938F99),
    error = Color(0xFFE46962),
    onError = Color.Black
)

@Composable
fun MyMoneyTheme(
    appTheme: AppTheme = AppTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme.colorTheme) {
        ColorTheme.DEFAULT -> if (appTheme.isDarkMode) greenDarkColorScheme() else greenLightColorScheme()
        ColorTheme.BLUE -> if (appTheme.isDarkMode) blueDarkColorScheme() else blueColorScheme()
        ColorTheme.PURPLE -> if (appTheme.isDarkMode) purpleDarkColorScheme() else purpleColorScheme()
        ColorTheme.ORANGE -> if (appTheme.isDarkMode) orangeDarkColorScheme() else orangeLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

enum class ColorTheme(val id: String, val displayNameRes: Int) {
    DEFAULT("default", R.string.theme_default),
    BLUE("blue", R.string.theme_blue),
    ORANGE("orange", R.string.theme_orange),
    PURPLE("purple", R.string.theme_purple);

    companion object {
        fun fromId(id: String): ColorTheme {
            return ColorTheme.entries.find { it.id == id } ?: DEFAULT
        }
    }
}

data class AppTheme(
    val colorTheme: ColorTheme = ColorTheme.DEFAULT,
    val isDarkMode: Boolean = false
) {
    fun toId(): String = "${colorTheme.id}_${if (isDarkMode) "dark" else "light"}"

    companion object {
        fun fromId(id: String): AppTheme {
            val parts = id.split("_")
            if (parts.size != 2) return AppTheme()

            val colorTheme = ColorTheme.fromId(parts[0])
            val isDarkMode = parts[1] == "dark"
            return AppTheme(colorTheme, isDarkMode)
        }
    }
}