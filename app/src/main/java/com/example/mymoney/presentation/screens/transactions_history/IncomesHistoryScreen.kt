package com.example.mymoney.presentation.screens.transactions_history

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mymoney.R
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.utils.DateUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomesHistoryScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: IncomesHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Моя история",
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
                    snackbarHostState.showSnackbar(effect.message)
                }

                HistorySideEffect.NavigateBack -> {
                    navHostController.popBackStack()
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