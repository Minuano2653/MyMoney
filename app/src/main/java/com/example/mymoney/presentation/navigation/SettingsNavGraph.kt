package com.example.mymoney.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mymoney.presentation.screens.settings.about.AboutScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.screens.settings.language.LanguageScreen

fun NavGraphBuilder.settingsNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToAboutApp: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToPassword: () -> Unit,
    onNavigateToHaptics: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Settings>(
        startDestination = SettingsScreen
    ) {
        composable<SettingsScreen> {
            SettingsScreen(
                onNavigateToAboutApp = onNavigateToAboutApp,
                onNavigateToLanguage = onNavigateToLanguage,
                onNavigateToPassword = onNavigateToPassword,
                onNavigateToHaptics = onNavigateToHaptics,
                modifier = modifier
            )
        }
        composable<AboutApp> {
            AboutScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
        composable<Language> {
            LanguageScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
    }
}