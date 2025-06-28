package com.example.mymoney.presentation.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmount
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    MyMoneyTheme {
        AccountScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            onShowSnackbar = {}
        )
    }
}

/**
 * Контейнер для экрана аккаунта, обрабатывающий состояние, события и сайд-эффекты.
 *
 * @param onUpdateTopAppBar Функция для обновления состояния верхней панели.
 * @param onUpdateFabState Функция для обновления состояния FAB.
 * @param onShowSnackbar Функция отображения Snackbar с сообщением.
 * @param viewModel ViewModel экрана аккаунта (по умолчанию внедряется через Hilt).
 */
@Composable
fun AccountScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                titleRes = R.string.top_bar_title_account,
                trailingIconRes = R.drawable.ic_edit,
                onTrailingClick = {
                    viewModel.handleEvent(AccountEvent.OnEditClicked)
                }
            )
        )
        onUpdateFabState(FabState(isVisible = true, onClick = {}))
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AccountSideEffect.ShowError -> {
                    onShowSnackbar(effect.message)
                }
                is AccountSideEffect.NavigateToEdit -> {
                    //TODO: навигация на редактирование счёта
                }
                is AccountSideEffect.NavigateToChangeCurrency-> {
                    //TODO: навигация на смену валюты
                }
            }
        }
    }

    AccountScreenContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
    )
}

/**
 * UI-содержимое экрана аккаунта, отображающее имя пользователя, баланс и валюту.
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param uiState Текущее состояние UI, предоставляемое ViewModel.
 * @param onEvent Обработчик пользовательских событий.
 */
@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    uiState: AccountUiState,
    onEvent: (AccountEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = uiState.name,
            trailingText = uiState.balance.formatAmount(),
            leadingIcon = {
                EmojiIcon(
                    "\uD83D\uDCB0",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            trailingIcon = {
                TrailingIcon()
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_currency),
            trailingText = uiState.currency.toSymbol(),
            trailingIcon = {
                TrailingIcon()
            },
            onClick = {onEvent(AccountEvent.OnCurrencyClicked)},
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
    }
}
