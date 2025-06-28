package com.example.mymoney.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Preview
@Composable
fun SettingsScreenPreview() {
    MyMoneyTheme {
        SettingsScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {}
        )
    }
}

/**
 * Экран настроек приложения.
 *
 * Отображает список настроек с возможностью переключения темы (ночной режим)
 * и выбора различных опций, таких как основной цвет, звуки, гаптика, пароль, синхронизация, язык и информация о приложении.
 *
 * @param modifier Модификатор для внешнего управления композаблом.
 * @param onUpdateTopAppBar Лямбда для обновления состояния верхнего бара (заголовок, иконки и действия).
 * @param onUpdateFabState Лямбда для обновления состояния кнопки FAB (видимость и действие).
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit
) {
    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                titleRes = R.string.top_bar_title_settings,
                onTrailingClick = {  }
            )
        )
        onUpdateFabState(
            FabState(
                isVisible = false,
                onClick = null
            )
        )
    }
    var isDarkMode by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        ListItem(
            modifier = modifier.height(56.dp),
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
            modifier = modifier
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
            modifier = modifier
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
            modifier = modifier
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
            modifier = modifier
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
            modifier = modifier
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
            modifier = modifier
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
            modifier = modifier
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
