package com.example.mymoney.presentation.screens.edit_transaction

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.base.viewmodel.daggerViewModel
import com.example.mymoney.presentation.components.AmountInputDialog
import com.example.mymoney.presentation.components.CategoriesBottomSheetContent
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.TimeInputDialog
import com.example.mymoney.presentation.screens.add_transaction.TransactionScreenContent
import com.example.mymoney.utils.DateUtils
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: EditTransactionViewModel = daggerViewModel()
) {
    var showCategorySheet by remember { mutableStateOf(false) }
    var showAmountDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
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

        TransactionScreenContent(
            modifier = modifier.padding(paddingValues),
            onCategoryClicked = { showCategorySheet = true },
            onAmountClicked = { showAmountDialog = true },
            onDateClicked = { showDatePicker = true },
            onTimeClicked = { showTimePicker = true },
            onValueChange = { comment ->
                viewModel.handleEvent(EditTransactionEvent.OnCommentChanged(comment))
            },
            accountName = uiState.account?.name,
            selectedCategory = uiState.selectedCategory?.name,
            amount = uiState.amount,
            currency = uiState.account?.currency,
            date = uiState.date,
            time = uiState.time,
            comment = uiState.comment
        )

        if (showCategorySheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showCategorySheet = false
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                CategoriesBottomSheetContent(
                    categories = uiState.categories,
                    onCategoryClicked = { category ->
                        viewModel.handleEvent(EditTransactionEvent.OnCategorySelected(category))
                        showCategorySheet = false
                    }
                )
            }
        }
        if (showAmountDialog) {
            AmountInputDialog(
                initialAmount = uiState.amount.replace(" ", ""),
                onConfirm = { amount ->
                    viewModel.handleEvent(EditTransactionEvent.OnAmountChanged(amount))
                    showAmountDialog = false
                },
                onDismiss = {
                    showAmountDialog = false
                }
            )
        }
        if (showDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.dayMonthYearToMillis(uiState.date),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatToDayMonthYear(it)
                        viewModel.handleEvent(EditTransactionEvent.OnDateSelected(date))
                        showDatePicker = false
                    }
                },
                onDismiss = { showDatePicker = false }
            )
        }
        if (showTimePicker) {
            val (hour, minute) = DateUtils.parseTimeToHourMinute(uiState.time)
            TimeInputDialog(
                hour = hour,
                minute = minute,
                onConfirm = { h, m ->
                    val formatted = "%02d:%02d".format(h, m)
                    viewModel.handleEvent(EditTransactionEvent.OnTimeSelected(formatted))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
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
