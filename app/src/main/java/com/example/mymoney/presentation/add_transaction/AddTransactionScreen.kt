package com.example.mymoney.presentation.add_transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.components.AmountInputDialog
import com.example.mymoney.presentation.components.CategoriesBottomSheetContent
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TimeInputDialog
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: TransactionDetailViewModel = hiltViewModel()
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
                    viewModel.handleEvent(TransactionDetailEvent.CancelChangesClicked)
                },
                onTrailingClick = {
                    viewModel.handleEvent(TransactionDetailEvent.SaveChangesClicked)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        TransactionScreenContent(
            uiState = uiState,
            onEvent = viewModel::handleEvent,
            modifier = modifier.padding(paddingValues)
        )

        if (uiState.showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.handleEvent(TransactionDetailEvent.DismissCategorySheet)
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                CategoriesBottomSheetContent(
                    categories = uiState.categories,
                    onCategoryClicked = { category ->
                        viewModel.handleEvent(TransactionDetailEvent.OnCategorySelected(category))
                        viewModel.handleEvent(TransactionDetailEvent.DismissCategorySheet)
                    }
                )
            }
        }
        if (uiState.showAmountDialog) {
            AmountInputDialog(
                initialAmount = uiState.amount,
                onConfirm = { amount ->
                    viewModel.handleEvent(TransactionDetailEvent.OnAmountChanged(amount))
                    viewModel.handleEvent(TransactionDetailEvent.DismissAmountDialog)
                },
                onDismiss = {
                    viewModel.handleEvent(TransactionDetailEvent.DismissAmountDialog)
                }
            )
        }
        if (uiState.showDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.dayMonthYearToMillis(uiState.date),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatToDayMonthYear(it)
                        viewModel.handleEvent(TransactionDetailEvent.OnDateSelected(date))
                        viewModel.handleEvent(TransactionDetailEvent.DismissDatePicker)
                    }
                },
                onDismiss = { viewModel.handleEvent(TransactionDetailEvent.DismissDatePicker) }
            )
        }
        if (uiState.showTimePicker) {
            val (hour, minute) = DateUtils.parseTimeToHourMinute(uiState.time)
            TimeInputDialog(
                hour = hour,
                minute = minute,
                onConfirm = { h, m ->
                    val formatted = "%02d:%02d".format(h, m)
                    viewModel.handleEvent(TransactionDetailEvent.OnTimeSelected(formatted))
                    viewModel.handleEvent(TransactionDetailEvent.DismissTimePicker)
                },
                onDismiss = { viewModel.handleEvent(TransactionDetailEvent.DismissTimePicker) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when(effect) {
                is TransactionDetailSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is TransactionDetailSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
}

@Composable
fun TransactionScreenContent(
    modifier: Modifier = Modifier,
    uiState: TransactionDetailUiState,
    onEvent: (TransactionDetailEvent) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = "Счет",
            trailingText = uiState.account?.name,
            trailingIcon = { TrailingIcon() },
        )
        Divider()
        ListItemComponent(
            title = "Статья",
            trailingText = uiState.selectedCategory?.name,
            trailingIcon = { TrailingIcon() },
            onClick = { onEvent(TransactionDetailEvent.ShowCategorySheet) }
        )
        Divider()
        ListItemComponent(
            title = "Cумма",
            trailingText = uiState.account?.let { "${uiState.amount} ${it.currency.toSymbol()}" } ?: uiState.amount,
            onClick = { onEvent(TransactionDetailEvent.ShowAmountDialog) }
        )
        Divider()
        ListItemComponent(
            title = "Дата",
            trailingText = uiState.date,
            onClick = { onEvent(TransactionDetailEvent.ShowDatePicker) }
        )
        Divider()
        ListItemComponent(
            title = "Время",
            trailingText = uiState.time,
            onClick = { onEvent(TransactionDetailEvent.ShowTimePicker) }
        )
        Divider()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.comment ?: "",
            onValueChange = { onEvent(TransactionDetailEvent.OnCommentChanged(it)) },
            placeholder = {
                Text(
                    text = "Комментарий",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RectangleShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Divider()
    }
}

@Preview
@Composable
fun TransactionDetailScreenPreview() {
    MyMoneyTheme {
        TransactionScreenContent(
            uiState = TransactionDetailUiState(),
            onEvent = {}
        )
    }
}