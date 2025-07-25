package com.example.mymoney.presentation.screens.settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.ListItemComponent
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AboutViewModel = daggerViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.settings_item_about,
                leadingIconRes = R.drawable.ic_arrow_back,
                onLeadingClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        AboutScreenContent(
            uiState = uiState,
            modifier = modifier.padding(paddingValues)
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AboutSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
            }
        }
    }
}

@Composable
fun AboutScreenContent(
    modifier: Modifier = Modifier,
    uiState: AboutUiState
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = stringResource(R.string.about_item_app_name),
            trailingText = uiState.appName,
            leadingIcon = {
                EmojiIcon(
                    "📱",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.about_item_version),
            trailingText = uiState.appVersion,
            leadingIcon = {
                EmojiIcon(
                    "🔢",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.about_item_last_update),
            trailingText = uiState.lastUpdateDate,
            trailingSubText = uiState.lastUpdateTime,
            leadingIcon = {
                EmojiIcon(
                    "📅",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    MyMoneyTheme {
        AboutScreenContent(
            uiState = AboutUiState(
                appName = "MyMoney",
                appVersion = "1.0.0 (1)",
                lastUpdateDate = "25.07.2025 12:00"
            )
        )
    }
}