package com.example.mymoney.presentation.screens.incomes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmount
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun IncomesScreenPreview() {
    MyMoneyTheme {
        IncomesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            onNavigateToHistory = {},
            onShowSnackbar = {}
        )
    }
}

/**
 * Экран отображения доходов.
 *
 * @param onUpdateTopAppBar функция для обновления состояния верхней панели приложения (TopAppBar).
 * @param onUpdateFabState функция для обновления состояния плавающей кнопки действия (FAB).
 * @param onNavigateToHistory функция для навигации на экран истории доходов.
 * @param onShowSnackbar suspend функция для отображения сообщений (snackbar).
 * @param viewModel ViewModel для управления состоянием экрана, предоставляемый Hilt.
 */
@Composable
fun IncomesScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onNavigateToHistory: (String) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: IncomesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                titleRes = R.string.top_bar_title_incomes,
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
                    onShowSnackbar(effect.message)
                }
                IncomesSideEffect.NavigateToHistory -> {
                    onNavigateToHistory(Screen.ROUTE_INCOMES_HISTORY)
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

/**
 * Контент экрана доходов.
 *
 * Отвечает за отображение общего итога и списка доходов.
 *
 * @param modifier модификатор для настройки компоновки.
 * @param uiState текущее состояние UI, содержащее данные и загрузку.
 * @param onEvent callback для обработки событий UI.
 */
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
                trailingText = uiState.total.formatAmount()
            )
            Divider()
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(uiState.incomes) { _, incomes ->
                    ListItemComponent(
                        title = incomes.category.name,
                        subtitle = incomes.comment,
                        trailingText = incomes.amount.formatAmount(),
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
