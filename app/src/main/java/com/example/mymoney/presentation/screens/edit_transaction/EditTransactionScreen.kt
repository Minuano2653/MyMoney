package com.example.mymoney.presentation.screens.edit_transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.base.viewmodel.provideViewModelFactory
import com.example.mymoney.presentation.components.AmountInputDialog
import com.example.mymoney.presentation.components.CategoriesBottomSheetContent
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.TimeInputDialog
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.screens.edit_account.EditAccountEvent
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    isIncome: Boolean,
    transactionId: Int,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: EditTransactionViewModel = viewModel(factory = provideViewModelFactory())
) {
    LaunchedEffect(Unit) {
        viewModel.handleEvent(EditTransactionEvent.SetInitData(isIncome, transactionId))
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_account,
                leadingIconRes = R.drawable.ic_cancel,
                trailingIconRes = R.drawable.ic_check,
                onLeadingClick = {
                    viewModel.handleEvent(EditTransactionEvent.CancelChanges)
                },
                onTrailingClick = {
                    viewModel.handleEvent(EditTransactionEvent.UpdateTransaction)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {
            TransactionScreenContent(
                uiState = uiState,
                onEvent = viewModel::handleEvent,
                modifier = modifier
            )
        }
        if (uiState.showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.handleEvent(EditTransactionEvent.DismissCategorySheet)
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                CategoriesBottomSheetContent(
                    categories = uiState.categories,
                    onCategoryClicked = { category ->
                        viewModel.handleEvent(EditTransactionEvent.OnCategorySelected(category))
                        viewModel.handleEvent(EditTransactionEvent.DismissCategorySheet)
                    }
                )
            }
        }
        if (uiState.showAmountDialog) {
            AmountInputDialog(
                initialAmount = uiState.amount.replace(" ", ""),
                onConfirm = { amount ->
                    viewModel.handleEvent(EditTransactionEvent.OnAmountChanged(amount))
                    viewModel.handleEvent(EditTransactionEvent.DismissAmountDialog)
                },
                onDismiss = {
                    viewModel.handleEvent(EditTransactionEvent.DismissAmountDialog)
                }
            )
        }
        if (uiState.showDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.dayMonthYearToMillis(uiState.date),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatToDayMonthYear(it)
                        viewModel.handleEvent(EditTransactionEvent.OnDateSelected(date))
                        viewModel.handleEvent(EditTransactionEvent.DismissDatePicker)
                    }
                },
                onDismiss = { viewModel.handleEvent(EditTransactionEvent.DismissDatePicker) }
            )
        }
        if (uiState.showTimePicker) {
            val (hour, minute) = DateUtils.parseTimeToHourMinute(uiState.time)
            TimeInputDialog(
                hour = hour,
                minute = minute,
                onConfirm = { h, m ->
                    val formatted = "%02d:%02d".format(h, m)
                    viewModel.handleEvent(EditTransactionEvent.OnTimeSelected(formatted))
                    viewModel.handleEvent(EditTransactionEvent.DismissTimePicker)
                },
                onDismiss = { viewModel.handleEvent(EditTransactionEvent.DismissTimePicker) }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is EditTransactionSideEffect.NavigateBack -> {
                    onNavigateBack()
                }

                is EditTransactionSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }
}

@Composable
fun TransactionScreenContent(
    modifier: Modifier = Modifier,
    uiState: EditTransactionUiState,
    onEvent: (EditTransactionEvent) -> Unit,
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
            onClick = { onEvent(EditTransactionEvent.ShowCategorySheet) }
        )
        Divider()
        ListItemComponent(
            title = "Cумма",
            trailingText = uiState.account?.let { "${uiState.amount} ${it.currency.toSymbol()}" } ?: uiState.amount,
            onClick = { onEvent(EditTransactionEvent.ShowAmountDialog) }
        )
        Divider()
        ListItemComponent(
            title = "Дата",
            trailingText = uiState.date,
            onClick = { onEvent(EditTransactionEvent.ShowDatePicker) }
        )
        Divider()
        ListItemComponent(
            title = "Время",
            trailingText = uiState.time,
            onClick = { onEvent(EditTransactionEvent.ShowTimePicker) }
        )
        Divider()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.comment ?: "",
            onValueChange = { onEvent(EditTransactionEvent.OnCommentChanged(it)) },
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
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            onClick = {
                onEvent(EditTransactionEvent.DeleteTransaction)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = if (uiState.isIncome) {
                    stringResource(R.string.button_delete_income)
                } else stringResource(R.string.button_delete_expense),
                color = MaterialTheme.colorScheme.onError
            )
        }

    }
}