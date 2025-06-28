package com.example.mymoney.presentation.screens.history.expenses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.screens.history.HistoryEvent
import com.example.mymoney.presentation.screens.history.HistoryScreenContent
import com.example.mymoney.presentation.screens.history.HistorySideEffect
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.DateUtils
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun HistoryScreenPreview() {
    MyMoneyTheme {
        ExpensesHistoryScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            onShowSnackbar = {},
            onNavigateBack = {}
        )
    }
}

/**
 * Экран истории расходов с выбором периода.
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param onUpdateTopAppBar Лямбда для обновления состояния верхнего AppBar.
 * @param onUpdateFabState Лямбда для обновления состояния FAB (плавающей кнопки действия).
 * @param onShowSnackbar Лямбда для отображения сообщения через Snackbar.
 * @param onNavigateBack Лямбда для обработки навигации назад.
 * @param viewModel ViewModel, управляющая состоянием и событиями экрана.
 */
@Composable
fun ExpensesHistoryScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ExpensesHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
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
        )
        onUpdateFabState(
            FabState(
                isVisible = false,
                onClick = null
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HistorySideEffect.ShowError -> {
                    onShowSnackbar(effect.message)
                }

                HistorySideEffect.NavigateBack -> {
                    onNavigateBack()
                }

                HistorySideEffect.NavigateToAnalysis -> {
                    // TODO: навигация на экран анализа
                }

                HistorySideEffect.ShowStartDatePicker -> {
                    showStartDatePicker = true
                }

                HistorySideEffect.ShowEndDatePicker -> {
                    showEndDatePicker = true
                }
            }
        }
    }

    HistoryScreenContent(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::handleEvent
    )

    if (showStartDatePicker) {
        DatePickerModal(
            initialSelectedDateMillis = DateUtils.toMillis(uiState.startDate),
            onDateSelected = { millis ->
                millis?.let {
                    val date = DateUtils.formatDateFromMillis(it)
                    viewModel.handleEvent(HistoryEvent.OnStartDateSelected(date))
                }
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        DatePickerModal(
            initialSelectedDateMillis = DateUtils.toMillis(uiState.endDate),
            onDateSelected = { millis ->
                millis?.let {
                    val date = DateUtils.formatDateFromMillis(it)
                    viewModel.handleEvent(HistoryEvent.OnEndDateSelected(date))
                }
            },
            onDismiss = { showEndDatePicker = false }
        )
    }
}
