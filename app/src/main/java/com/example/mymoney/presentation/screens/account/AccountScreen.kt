package com.example.mymoney.presentation.screens.account

import androidx.compose.foundation.layout.Column
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
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.formatAmountWithCurrency
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccountScreen(
    onNavigateToEditAccount: (Int) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AccountViewModel = hiltViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_account,
                trailingIconRes = R.drawable.ic_edit,
                onTrailingClick = {
                    viewModel.handleEvent(AccountEvent.OnEditClicked)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        AccountScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AccountSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is AccountSideEffect.NavigateToEditAccount -> {
                    onNavigateToEditAccount(38) //TODO: пока захардкожено
                }
            }
        }
    }
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
            trailingText = uiState.balance.formatAmountWithCurrency(),
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
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    MyMoneyTheme {
        AccountScreenContent(
            uiState = AccountUiState(),
            onEvent = {}
        )
    }
}
