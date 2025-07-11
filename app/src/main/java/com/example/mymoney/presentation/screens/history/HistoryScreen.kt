package com.example.mymoney.presentation.screens.history

import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.emptyPreferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.screens.history.HistoryViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.DateUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToEditTransaction: (Int) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: HistoryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.handleEvent(HistoryEvent.LoadTransactions)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_history,
                leadingIconRes = R.drawable.ic_arrow_back,
                trailingIconRes = R.drawable.ic_analysis,
                onLeadingClick = {
                    viewModel.handleEvent(HistoryEvent.OnBackPressed)
                },
                onTrailingClick = {
                    viewModel.handleEvent(HistoryEvent.OnAnalysisClicked)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        HistoryScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )

        if (uiState.showStartDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.startDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(HistoryEvent.OnStartDateSelected(date))
                    }
                },
                onDismiss = { viewModel.handleEvent(HistoryEvent.OnStartDateClicked) }
            )
        }

        if (uiState.showEndDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.endDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(HistoryEvent.OnEndDateSelected(date))
                    }
                },
                onDismiss = { viewModel.handleEvent(HistoryEvent.OnEndDateClicked) }
            )
        }

    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HistorySideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is HistorySideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is HistorySideEffect.NavigateToEditTransaction -> {
                    onNavigateToEditTransaction(effect.transactionId)
                }
                is HistorySideEffect.NavigateToAnalysis -> {
                    // TODO: навигация на экран анализа
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    MyMoneyTheme {
        HistoryScreen(
            onNavigateBack = {},
            onNavigateToEditTransaction = {}
        )
    }
}