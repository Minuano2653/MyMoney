package com.example.mymoney.presentation.screens.edit_account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CurrencyBottomSheetContent
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: EditAccountViewModel = hiltViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_account,
                leadingIconRes = R.drawable.ic_cancel,
                trailingIconRes = R.drawable.ic_check,
                onLeadingClick = {
                    viewModel.handleEvent(EditAccountEvent.OnCancelChangesClicked)
                },
                onTrailingClick = {
                    viewModel.handleEvent(EditAccountEvent.OnSaveChangesClicked)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        EditAccountScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )

        if (uiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.handleEvent(EditAccountEvent.OnBottomSheetDismissed)
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                CurrencyBottomSheetContent(
                    onCurrencyClick = { currency ->
                        viewModel.handleEvent(EditAccountEvent.OnCurrencyChanged(currency))
                    },
                    onCancelClick = {
                        viewModel.handleEvent(EditAccountEvent.OnBottomSheetDismissed)
                    }
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when(effect) {
                is EditAccountSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is EditAccountSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
}

@Composable
fun EditAccountScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditAccountUiState,
    onEvent: (EditAccountEvent) -> Unit
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.name,
            onValueChange = { onEvent(EditAccountEvent.OnNameChanged(it)) },
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = "Баланс"
                )
            },
            placeholder = {
                Text(
                    text = "Название счёта",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RectangleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            enabled = !uiState.isSaving
        )
        Divider()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.balance,
            onValueChange = { onEvent(EditAccountEvent.OnBalanceChanged(it)) },
            placeholder = {
                Text(
                    text = "Баланс",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RectangleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = !uiState.isSaving
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_currency),
            trailingText = uiState.currency.toSymbol(),
            trailingIcon = {
                TrailingIcon()
            },
            onClick = {
                if (!uiState.isSaving) {
                    onEvent(EditAccountEvent.OnCurrencyClicked)
                }
            },
            itemHeight = 56.dp,
            backgroundColor = Color.White,
        )
        Divider()
        if (uiState.isSaving) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview()
@Composable
fun EditAccountScreenContentPreview() {
    MyMoneyTheme {
        EditAccountScreenContent(
            uiState = EditAccountUiState(),
            onEvent = {}
        )
    }
}