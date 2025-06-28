package com.example.mymoney.presentation.screens.expenses

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmount
import kotlinx.coroutines.flow.collectLatest


@Preview
@Composable
fun ExpensesScreenPreview() {
    MyMoneyTheme {
        ExpensesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            onNavigateToHistory = {},
            onShowSnackbar = {}
        )
    }
}

/**
 * Обёртка экрана расходов.
 *
 * Отображает список расходов, заголовок и кнопку добавления нового расхода.
 * Обрабатывает навигацию, ошибки и взаимодействия пользователя через ViewModel.
 *
 * @param onUpdateTopAppBar Функция для обновления состояния верхней панели приложения.
 * @param onUpdateFabState Функция для обновления состояния плавающей кнопки действий (FAB).
 * @param onNavigateToHistory Функция навигации к экрану истории расходов.
 * @param onShowSnackbar Функция для показа сообщений пользователю (snackbar).
 * @param viewModel ViewModel экрана расходов (по умолчанию внедряется через Hilt).
 */
@Composable
fun ExpensesScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onNavigateToHistory: (String) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                titleRes = R.string.top_bar_title_expenses,
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
                    onShowSnackbar(effect.message)
                }
                ExpensesSideEffect.NavigateToHistory -> {
                    onNavigateToHistory(Screen.ROUTE_EXPENSES_HISTORY)
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

/**
 * Контентная часть экрана расходов.
 *
 * Показывает общее количество расходов и список отдельных записей расходов.
 * Отображает индикатор загрузки при необходимости.
 *
 * @param modifier Модификатор Compose для настройки компоновки.
 * @param uiState Текущее состояние UI с данными расходов.
 * @param onEvent Обработчик событий UI, например, кликов.
 */
@OptIn(ExperimentalMaterial3Api::class)
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
                        trailingIcon = { TrailingIcon() }
                    )
                    Divider()
                }
            }
        }
    }
}
