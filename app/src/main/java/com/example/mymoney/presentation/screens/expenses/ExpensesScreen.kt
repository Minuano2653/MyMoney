package com.example.mymoney.presentation.screens.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.components.CustomFloatingActionButton
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmount
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExpensesScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToTransactionDetail: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ExpensesViewModel = hiltViewModel(),
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
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        ExpensesScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ExpensesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is ExpensesSideEffect.NavigateToHistory -> {
                    onNavigateToHistory()
                }
                is ExpensesSideEffect.NavigateToAddExpense -> {
                    /*onNavigateToAddExpense()*/
                }
                is ExpensesSideEffect.NavigateToTransactionDetail -> {
                    /*onNavigateToTransactionDetail()*/
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreenContent(
    modifier: Modifier = Modifier,
    uiState: ExpensesUiState,
    onEvent: (ExpensesEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ListItemComponent(
                title = stringResource(R.string.list_item_text_total),
                trailingText = uiState.total.formatAmount(),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp
            )
            Divider()
            LazyColumn {
                itemsIndexed(uiState.expenses) { _, expense ->
                    ListItemComponent(
                        title = expense.category.name,
                        subtitle = expense.comment,
                        trailingText = expense.amount.formatAmount(),
                        leadingIcon = { EmojiIcon(emoji = expense.category.emoji) },
                        trailingIcon = { TrailingIcon() },
                        onClick = { onEvent(ExpensesEvent.OnTransactionClicked) }
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
        ExpensesScreenContent(
            uiState = ExpensesUiState(),
            onEvent = {}
        )
    }
}