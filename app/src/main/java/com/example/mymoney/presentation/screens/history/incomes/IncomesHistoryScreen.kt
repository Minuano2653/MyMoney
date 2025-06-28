package com.example.mymoney.presentation.screens.history.incomes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.screens.history.HistoryEvent
import com.example.mymoney.presentation.screens.history.HistoryScreenContent
import com.example.mymoney.presentation.screens.history.HistorySideEffect
import com.example.mymoney.utils.DateUtils
import kotlinx.coroutines.flow.collectLatest

/**
 * Экран истории доходов с выбором периода и управлением состояниями.
 *
 * Отображает список доходов за выбранный период, предоставляет возможность
 * выбрать даты начала и конца периода через модальные окна выбора даты.
 * Управляет состоянием верхней панели, плавающей кнопки и побочными эффектами,
 * такими как отображение сообщений и навигация.
 *
 * @param modifier Модификатор для кастомизации внешнего вида компонента.
 * @param onUpdateTopAppBar Функция для обновления состояния верхней панели приложения.
 * @param onUpdateFabState Функция для обновления состояния плавающей кнопки.
 * @param onShowSnackbar Лямбда для отображения snackbar с сообщением (например, ошибки).
 * @param onNavigateBack Лямбда для навигации назад.
 * @param viewModel ViewModel, управляющая состоянием экрана.
 */
@Composable
fun IncomesHistoryScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: IncomesHistoryViewModel = hiltViewModel()
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
