package com.example.mymoney.presentation.screens.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.mymoney.R
import com.example.core.ui.components.CustomFloatingActionButton
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.ListItemComponent
import com.example.core.ui.components.LoadingCircularIndicator
import com.example.core.ui.components.TrailingIcon
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.core.common.utils.formatAmount
import com.example.core.common.utils.toSymbol
import com.example.core.domain.entity.Transaction
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExpensesScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToTransactionDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ExpensesViewModel = daggerViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_expenses,
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {
                    viewModel.handleEvent(ExpensesEvent.OnHistoryClicked)
                }
            )
        },
        floatingActionButton = {
            CustomFloatingActionButton(
                onClick = {
                    viewModel.handleEvent(ExpensesEvent.OnAddClicked)
                },
                iconRes = R.drawable.ic_add,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TransactionsScreenContent(
            isLoading = uiState.isLoading,
            transactions = uiState.expenses,
            onExpenseClick = { expense ->
                viewModel.handleEvent(ExpensesEvent.OnTransactionClicked(expense))
            },
            total = uiState.total.formatAmount(),
            currency = uiState.currency.toSymbol(),
            modifier = modifier.padding(paddingValues),
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ExpensesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
                is ExpensesSideEffect.NavigateToHistory -> {
                    onNavigateToHistory()
                }
                is ExpensesSideEffect.NavigateToAddExpense -> {
                    onNavigateToAddExpense()
                }
                is ExpensesSideEffect.NavigateToTransactionDetail -> {
                    onNavigateToTransactionDetail(effect.transactionId)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<Transaction>,
    onExpenseClick: (Transaction) -> Unit,
    total: String,
    currency: String,
) {
    if (isLoading) {
        LoadingCircularIndicator()
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ListItemComponent(
                title = stringResource(R.string.list_item_text_total),
                trailingText = "$total $currency",
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp
            )
            Divider()
            LazyColumn {
                itemsIndexed(
                    transactions,
                    key = { _, expense -> expense.id }
                ) { _, expense ->
                    ListItemComponent(
                        title = expense.category.name,
                        subtitle = expense.comment,
                        trailingText = "${expense.amount.formatAmount()} $currency",
                        leadingIcon = { EmojiIcon(emoji = expense.category.emoji) },
                        trailingIcon = { TrailingIcon(R.drawable.ic_more_vert) },
                        onClick = { onExpenseClick(expense) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun ExpensesScreenContentPreview() {
    MyMoneyTheme {
        TransactionsScreenContent(
            transactions = emptyList(),
            onExpenseClick = { },
            total = "1111",
            currency = "RUB",
            isLoading = false
        )
    }
}