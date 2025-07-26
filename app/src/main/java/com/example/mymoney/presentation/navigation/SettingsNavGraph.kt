package com.example.mymoney.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mymoney.presentation.screens.settings.about.AboutScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.screens.settings.language.LanguageScreen
import com.example.mymoney.presentation.screens.settings.main_color.MainColorScreen
import com.example.mymoney.presentation.screens.settings.pincode.create.CreatePinScreen

fun NavGraphBuilder.settingsNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToAboutApp: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToPrimaryColor: () -> Unit,
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
                onNavigateToPrimaryColor = onNavigateToPrimaryColor,
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
        composable<PrimaryColors> {
            MainColorScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
        composable<Password> {
            CreatePinScreen(
                modifier = modifier,
                onNavigateBack = onNavigateBack,
            )
        }
    }
}