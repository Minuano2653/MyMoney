package com.example.mymoney.presentation.screens.incomes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.CustomFloatingActionButton
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmountWithCurrency
import kotlinx.coroutines.flow.collectLatest

@Composable
fun IncomesScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddIncome: () -> Unit,
    onNavigateToTransactionDetail: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: IncomesViewModel = hiltViewModel(),
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

        IncomesScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
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
                    /*onNavigateToAddIncome()*/
                }
                is IncomesSideEffect.NavigateToTransactionDetail -> {
                    /*onNavigateToTransactionDetail()*/
                }
            }
        }
    }
}

@Composable
fun IncomesScreenContent(
    modifier: Modifier = Modifier,
    uiState: IncomesUiState,
    onEvent: (IncomesEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ListItemComponent(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp,
                title = "Всего",
                trailingText = uiState.total.formatAmountWithCurrency()
            )
            Divider()
            LazyColumn {
                itemsIndexed(uiState.incomes) { _, incomes ->
                    ListItemComponent(
                        title = incomes.category.name,
                        subtitle = incomes.comment,
                        trailingText = incomes.amount.formatAmountWithCurrency(),
                        leadingIcon = {
                            EmojiIcon(emoji = incomes.category.emoji)
                        },
                        trailingIcon = {
                            TrailingIcon()
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview
@Composable
fun IncomesScreenPreview() {
    MyMoneyTheme {
        IncomesScreenContent(
            uiState = IncomesUiState(),
            onEvent = {}
        )
    }
}
