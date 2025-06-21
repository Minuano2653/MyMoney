package com.example.mymoney.presentation.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.R
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    MyMoneyTheme {
        AccountScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {},
            navHostController = rememberNavController(),
            snackbarHostState = SnackbarHostState()
        )
    }
}

@Composable
fun AccountScreen(
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit,
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Мой счет",
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
                    snackbarHostState.showSnackbar(effect.message)
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

@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    uiState: AccountUiState,
    onEvent: (AccountEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = uiState.name,
            trailingText = uiState.balance.toCurrency(),
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

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ListItemComponent(
            title = "Валюта",
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

/*
private fun getMockAccount(): AccountUiState = AccountUiState(
    Account(1, "Баланс", (-670000.00).toBigDecimal(), "RUB")
)*/
