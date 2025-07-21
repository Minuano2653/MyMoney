package com.example.mymoney.presentation.screens.incomes

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.base.viewmodel.daggerViewModel
import com.example.mymoney.presentation.components.CustomFloatingActionButton
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.screens.expenses.TransactionsScreenContent
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmount
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomesScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddIncome: () -> Unit,
    onNavigateToTransactionDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: IncomesViewModel = daggerViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_incomes,
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {
                    viewModel.handleEvent(IncomesEvent.OnHistoryClicked)
                }
            )
        },
        floatingActionButton = {
            CustomFloatingActionButton(
                onClick = {
                    viewModel.handleEvent(IncomesEvent.OnAddClicked)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        TransactionsScreenContent(
            isLoading = uiState.isLoading,
            transactions = uiState.incomes,
            onExpenseClick = { expense ->
                viewModel.handleEvent(IncomesEvent.OnTransactionClicked(expense))
            },
            total = uiState.total.formatAmount(),
            currency = uiState.currency.toSymbol(),
            modifier = modifier.padding(paddingValues),
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is IncomesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is IncomesSideEffect.NavigateToHistory -> {
                    onNavigateToHistory()
                }
                is IncomesSideEffect.NavigateToAddIncome -> {
                    onNavigateToAddIncome()
                }
                is IncomesSideEffect.NavigateToTransactionDetail -> {
                    onNavigateToTransactionDetail(effect.transactionId)
                }
            }
        }
    }
}

@Preview
@Composable
fun IncomesScreenContentPreview() {
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