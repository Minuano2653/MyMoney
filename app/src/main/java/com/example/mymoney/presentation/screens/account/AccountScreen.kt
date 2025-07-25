package com.example.mymoney.presentation.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.utils.formatAmount
import com.example.core.common.utils.toSymbol
import com.example.core.ui.charts.bar.BarChart
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.ListItemComponent
import com.example.core.ui.components.TrailingIcon
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccountScreen(
    onNavigateToEditAccount: (Int) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AccountViewModel = daggerViewModel()
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
            modifier = modifier.padding(paddingValues)
        )
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AccountSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
                is AccountSideEffect.NavigateToEditAccount -> {
                    onNavigateToEditAccount(effect.accountId)
                }
            }
        }
    }
}

@Composable
fun AccountScreenContent(
    modifier: Modifier = Modifier,
    uiState: AccountUiState,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = uiState.name,
            trailingText = "${uiState.balance.formatAmount()} ${uiState.currency.toSymbol()}",
            leadingIcon = {
                EmojiIcon(
                    "\uD83D\uDCB0",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            trailingIcon = {
                TrailingIcon(R.drawable.ic_more_vert)
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_currency),
            trailingText = uiState.currency.toSymbol(),
            trailingIcon = {
                TrailingIcon(R.drawable.ic_more_vert)
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.chartItems.isNotEmpty()) {
            BarChart(
                modifier = Modifier.padding(8.dp),
                data = uiState.chartItems
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    MyMoneyTheme {
        AccountScreenContent(
            uiState = AccountUiState(),
        )
    }
}
