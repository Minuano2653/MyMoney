package com.example.mymoney.presentation.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.entity.Transaction
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.DatePickerModal
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.EmptyContent
import com.example.core.ui.components.ListItemComponent
import com.example.core.ui.components.LoadingCircularIndicator
import com.example.core.ui.components.TrailingIcon
import com.example.core.common.utils.DateUtils
import com.example.core.common.utils.formatAmount
import com.example.core.common.utils.toSymbol
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToEditTransaction: (Int) -> Unit,
    onNavigateToAnalysis: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: HistoryViewModel = daggerViewModel()
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
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
            isLoading = uiState.isLoading,
            transactions = uiState.transactions,
            total = uiState.total,
            currency = uiState.currency,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            onStartDateClick = { showStartDatePicker = true },
            onEndDateClick = { showEndDatePicker = true },
            onTransactionClick = { transaction -> viewModel.handleEvent(HistoryEvent.OnTransactionClicked(transaction))},
            modifier = modifier.padding(paddingValues)
        )

        if (showStartDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.startDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(HistoryEvent.OnStartDateSelected(date))
                    }
                    showStartDatePicker = false
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
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HistorySideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
                is HistorySideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is HistorySideEffect.NavigateToEditTransaction -> {
                    onNavigateToEditTransaction(effect.transactionId)
                }
                is HistorySideEffect.NavigateToAnalysis -> {
                    onNavigateToAnalysis(effect.isIncome)
                }
            }
        }
    }
}

@Composable
fun HistoryScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<Transaction>,
    total: BigDecimal,
    currency: String,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        HistoryFilterSection(
            startDate = startDate,
            endDate = endDate,
            total = total,
            currency = currency,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick
        )

        when {
            isLoading -> {
                LoadingCircularIndicator(modifier = Modifier.fillMaxSize())
            }
            transactions.isEmpty() -> {
                EmptyContent(
                    noContentLabel = R.string.no_history_data,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                HistoryContent(
                    transactions = transactions,
                    currency = currency,
                    onTransactionClick = onTransactionClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun HistoryFilterSection(
    startDate: String,
    endDate: String,
    total: BigDecimal,
    currency: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItemComponent(
            title = stringResource(R.string.list_item_text_start),
            trailingText = startDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = onStartDateClick
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_end),
            trailingText = endDate,
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            onClick = onEndDateClick
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_sum),
            trailingText = "${total.formatAmount()} ${currency.toSymbol()}",
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Divider()
    }
}

@Composable
fun HistoryContent(
    transactions: List<Transaction>,
    currency: String,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = transactions,
            key = { _, transaction -> transaction.id }
        ) { _, transaction ->
            ListItemComponent(
                title = transaction.category.name,
                subtitle = transaction.comment,
                trailingText = "${transaction.amount.formatAmount()} ${currency.toSymbol()}",
                trailingSubText = DateUtils.formatIsoToDayMonth(transaction.transactionDate),
                leadingIcon = {
                    EmojiIcon(emoji = transaction.category.emoji)
                },
                trailingIcon = {
                    TrailingIcon(R.drawable.ic_more_vert)
                },
                onClick = {
                    onTransactionClick(transaction)
                }
            )
            Divider()
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    MyMoneyTheme {
        HistoryScreen(
            onNavigateBack = {},
            onNavigateToEditTransaction = {},
            onNavigateToAnalysis = {}
        )
    }
}