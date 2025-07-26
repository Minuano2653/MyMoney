package com.example.mymoney.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.screens.settings.theme.ThemeEvent
import com.example.mymoney.presentation.screens.settings.theme.ThemeSideEffect
import com.example.mymoney.presentation.screens.settings.theme.ThemeUiState
import com.example.mymoney.presentation.screens.settings.theme.ThemeViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SettingsScreen(
    onNavigateToAboutApp: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToPassword: () -> Unit,
    onNavigateToHaptics: () -> Unit,
    onNavigateToPrimaryColor: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    themeViewModel: ThemeViewModel = daggerViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_settings
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by themeViewModel.uiState.collectAsStateWithLifecycle()

        SettingsScreenContent(
            navigateToAboutApp = onNavigateToAboutApp,
            navigateToLanguage = onNavigateToLanguage,
            navigateToPassword = onNavigateToPassword,
            navigateToHaptics = onNavigateToHaptics,
            navigateToPrimaryColor = onNavigateToPrimaryColor,
            onThemeToggle = { isDarkMode ->
                themeViewModel.handleEvent(ThemeEvent.ToggleDarkMode(isDarkMode))
            },
            modifier = modifier.padding(paddingValues),
            themeUiState = uiState
        )
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        themeViewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ThemeSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
            }
        }
    }
}

@Composable
fun SettingsScreenContent(
    navigateToLanguage: () -> Unit,
    navigateToAboutApp: () -> Unit,
    navigateToPrimaryColor: () -> Unit,
    navigateToPassword: () -> Unit,
    navigateToHaptics: () -> Unit,
    themeUiState: ThemeUiState,
    onThemeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItem(
            modifier = Modifier.height(56.dp),
            headlineContent = { Text(stringResource(R.string.settings_item_night_theme)) },
            trailingContent = {
                Switch(
                    checked = themeUiState.currentTheme.isDarkMode,
                    onCheckedChange = onThemeToggle,
                    enabled = !themeUiState.isLoading,
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { navigateToPrimaryColor() },
            headlineContent = { Text(stringResource(R.string.settings_item_primary_color)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { },
            headlineContent = { Text(stringResource(R.string.settings_item_sounds)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { navigateToHaptics() },
            headlineContent = { Text(stringResource(R.string.settings_item_haptic)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { navigateToPassword() },
            headlineContent = { Text(stringResource(R.string.settings_item_password)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { },
            headlineContent = { Text(stringResource(R.string.settings_item_sync)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { navigateToLanguage() },
            headlineContent = { Text(stringResource(R.string.settings_item_lang)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { navigateToAboutApp() },
            headlineContent = { Text(stringResource(R.string.settings_item_about)) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Divider()
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    MyMoneyTheme {
        SettingsScreenContent(
            navigateToAboutApp = {},
            navigateToLanguage = {},
            navigateToPassword = {},
            navigateToHaptics = {},
            onThemeToggle = {},
            themeUiState = ThemeUiState(),
            navigateToPrimaryColor = {}
        )
    }
}

