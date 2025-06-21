package com.example.mymoney.presentation.screens.incomes

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.R
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun IncomesScreenPreview() {
    MyMoneyTheme {
        IncomesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            navHostController = rememberNavController(),
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Composable
fun IncomesScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: IncomesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Доходы сегодня",
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {
                    viewModel.handleEvent(IncomesEvent.OnHistoryClicked)
                }
            )
        )
        onUpdateFabState(
            FabState(
                isVisible = true,
                onClick = { viewModel.handleEvent(IncomesEvent.OnAddClicked) }
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is IncomesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                IncomesSideEffect.NavigateToHistory -> {
                    navHostController.navigate(Screen.ROUTE_INCOMES_HISTORY)
                }
                is IncomesSideEffect.NavigateToAddExpense -> {
                    //TODO: навигация на экран добавления расхода
                }
            }
        }
    }

    IncomesScreenContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
fun IncomesScreenContent(
    modifier: Modifier = Modifier,
    uiState: IncomesUiState,
    onEvent: (IncomesEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ListItemComponent(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp,
                title = "Всего",
                trailingText = uiState.total.toCurrency()
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.incomes) { _, incomes ->
                    ListItemComponent(
                        title = incomes.category.name,
                        subtitle = incomes.comment,
                        trailingText = incomes.amount.toCurrency(),
                        leadingIcon = {
                            EmojiIcon(emoji = incomes.category.emoji)
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
private fun getMockIncomesUiState() = IncomesUiState(
    incomes = listOf(
        Transaction(
            id = 1,
            category = Category(1, "Зарплата", "\uD83D\uDCB0", isIncome = true),
            amount = 500000.00.toBigDecimal(),
            createdAt = "2025-01-01",
            updatedAt = "2025-01-01"
        ),
        Transaction(
            id = 2,
            category = Category(2, "Подработка", "\uD83D\uDCB0", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        )
    )
)*/
