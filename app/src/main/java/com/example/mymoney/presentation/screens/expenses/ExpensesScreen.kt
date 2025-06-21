package com.example.mymoney.presentation.screens.expenses

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
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Transaction
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency
import kotlinx.coroutines.flow.collectLatest


@Preview
@Composable
fun ExpensesScreenPreview() {
    MyMoneyTheme {
        ExpensesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            navHostController = rememberNavController(),
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Composable
fun ExpensesScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Расходы сегодня",
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {
                    viewModel.handleEvent(ExpensesEvent.OnHistoryClicked)
                }
            )
        )
        onUpdateFabState(
            FabState(
            isVisible = true,
            onClick = { viewModel.handleEvent(ExpensesEvent.OnAddClicked) }
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ExpensesSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                ExpensesSideEffect.NavigateToHistory -> {
                    navHostController.navigate(Screen.ROUTE_EXPENSES_HISTORY)
                }
                is ExpensesSideEffect.NavigateToAddExpense -> {
                    //TODO: навигация на экран добавления расхода
                }
            }
        }
    }

    ExpensesScreenContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
    )
}

@Composable
fun ExpensesScreenContent(
    modifier: Modifier = Modifier,
    uiState: ExpensesUiState,
    onEvent: (ExpensesEvent) -> Unit,
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            ListItemComponent(
                title = "Всего",
                trailingText = uiState.total.toCurrency(),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                itemHeight = 56.dp
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            LazyColumn {
                itemsIndexed(uiState.expenses) { _, expense ->
                    ListItemComponent(
                        title = expense.category.name,
                        subtitle = expense.comment,
                        trailingText = expense.amount.toCurrency(),
                        leadingIcon = { EmojiIcon(emoji = expense.category.emoji) },
                        trailingIcon = { TrailingIcon() }
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

/*@Composable
fun ExpensesScreenContent(
    modifier: Modifier = Modifier,
    uiState: ExpensesUiState,
    onEvent: (ExpensesEvent) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        ListItemComponent(
            title = "Всего",
            trailingText = uiState.total.toCurrency(),
            backgroundColor = MaterialTheme.colorScheme.secondary,
            itemHeight = 56.dp
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(uiState.expenses) { _, expense ->
                ListItemComponent(
                    title = expense.category.name,
                    subtitle = expense.comment,
                    trailingText = expense.amount.toCurrency(),
                    leadingIcon = {
                        EmojiIcon(emoji = expense.category.emoji)
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
}*/

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
