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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Preview
@Composable
fun SettingsScreenPreview() {
    MyMoneyTheme {
        SettingsScreenContent()
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_settings,
                onTrailingClick = {  }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        SettingsScreenContent(
            modifier = modifier.padding(paddingValues)
        )
    }
}


@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
) {
    var isDarkMode by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        ListItem(
            modifier = Modifier.height(56.dp),
            headlineContent = { Text(stringResource(R.string.settings_item_night_theme)) },
            trailingContent = {
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it },
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
                .clickable{ },
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
                .clickable{ },
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
                .clickable{ },
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
                .clickable{ },
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
                .clickable{ },
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
                .clickable{ },
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
                .clickable{ },
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
