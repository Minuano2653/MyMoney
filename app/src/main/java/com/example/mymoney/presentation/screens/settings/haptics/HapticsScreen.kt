package com.example.mymoney.presentation.screens.settings.haptics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.entity.AppLanguage
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.screens.settings.language.LanguageEvent
import com.example.mymoney.presentation.screens.settings.language.LanguageScreenContent
import com.example.mymoney.presentation.screens.settings.language.LanguageSideEffect
import com.example.mymoney.presentation.screens.settings.language.LanguageUiState
import com.example.mymoney.presentation.screens.settings.language.LanguageViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HapticsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: LanguageViewModel = daggerViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.settings_item_haptic,
                leadingIconRes = R.drawable.ic_arrow_back,
                onLeadingClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LanguageScreenContent(
            uiState = uiState,
            onLanguageSelected = { language ->
                viewModel.handleEvent(LanguageEvent.SelectLanguage(language))
            },
            modifier = modifier.padding(paddingValues)
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is LanguageSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
            }
        }
    }
}

@Composable
fun HapticsScreenContent(
    uiState: LanguageUiState,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            AppLanguage.entries.forEach { language ->
                ListItem(
                    modifier = Modifier
                        .height(56.dp)
                        .clickable { onLanguageSelected(language) },
                    headlineContent = {
                        Text(language.displayName)
                    },
                    trailingContent = {
                        RadioButton(
                            selected = uiState.selectedLanguage == language,
                            onClick = { onLanguageSelected(language) }
                        )
                    }
                )
                Divider()
            }
        }
    }
}