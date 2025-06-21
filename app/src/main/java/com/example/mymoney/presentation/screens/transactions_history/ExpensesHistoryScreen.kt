package com.example.mymoney.presentation.screens.transactions_history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.R
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.presentation.screens.expenses.ExpensesUiState
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.toCurrency
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun HistoryScreenPreview() {
    MyMoneyTheme {
        ExpensesHistoryScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            navHostController = rememberNavController(),
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Composable
fun ExpensesHistoryScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: ExpensesHistoryViewModel = hiltViewModel()
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

@Composable
fun HistoryScreenContent(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = "Начало",
            trailingText = uiState.startDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = { onEvent(HistoryEvent.OnStartDateClicked) }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ListItemComponent(
            title = "Конец",
            trailingText = uiState.endDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = { onEvent(HistoryEvent.OnEndDateClicked) }
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ListItemComponent(
            title = "Сумма",
            trailingText = uiState.total.toCurrency(),
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.transactions) { _, transaction ->
                    ListItemComponent(
                        title = transaction.category.name,
                        subtitle = transaction.comment,
                        trailingText = transaction.amount.toCurrency(),
                        trailingSubText = DateUtils.formatIsoToDayMonth(transaction.transactionDate),
                        leadingIcon = {
                            EmojiIcon(emoji = transaction.category.emoji)
                        },
                        trailingIcon = {
                            TrailingIcon()
                        }
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

/*
private fun getMockExpensesUiState() = ExpensesUiState(
    expenses = listOf(
        Transaction(
            id = 1,
            category = Category(1, "Аренда квартиры", "🏡", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-01",
            updatedAt = "2025-01-01"
        ),
        Transaction(
            id = 2,
            category = Category(2, "Одежда", "👗", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 3,
            category = Category(3, "На собачку", "🐶", isIncome = false),
            comment = "Джек",
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 4,
            category = Category(3, "На собачку", "🐶", isIncome = false),
            comment = "Энни",
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 5,
            category = Category(4, "Ремонт квартиры", "РК", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 6,
            category = Category(5, "Продукты", "🍭", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 7,
            category = Category(6, "Спортзал", "🏋️", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Transaction(
            id = 8,
            category = Category(7, "Медицина", "💊", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        )
    )
)*/
