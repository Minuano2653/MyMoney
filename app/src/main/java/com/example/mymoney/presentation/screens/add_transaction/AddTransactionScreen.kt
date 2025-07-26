package com.example.mymoney.presentation.screens.add_transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.utils.DateUtils
import com.example.core.common.utils.toSymbol
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.DatePickerModal
import com.example.core.ui.components.Divider
import com.example.core.ui.components.ListItemComponent
import com.example.core.ui.components.TimeInputDialog
import com.example.core.ui.components.TrailingIcon
import com.example.mymoney.R
import com.example.mymoney.presentation.components.AmountInputDialog
import com.example.mymoney.presentation.components.CategoriesBottomSheetContent
import com.example.mymoney.presentation.daggerViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    isIncome: Boolean,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AddTransactionViewModel = daggerViewModel()
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
                titleRes = if (isIncome) R.string.top_bar_title_my_incomes else R.string.top_bar_title_my_expenses ,
                leadingIconRes = R.drawable.ic_cancel,
                trailingIconRes = R.drawable.ic_check,
                onLeadingClick = {
                    viewModel.handleEvent(AddTransactionEvent.CancelChangesClicked)
                },
                onTrailingClick = {
                    viewModel.handleEvent(AddTransactionEvent.SaveChangesClicked)
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
                viewModel.handleEvent(AddTransactionEvent.OnCommentChanged(comment))
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
                        viewModel.handleEvent(AddTransactionEvent.OnCategorySelected(category))
                        showCategorySheet = false
                    }
                )
            }
        }
        if (showAmountDialog) {
            AmountInputDialog(
                initialAmount = uiState.amount,
                onConfirm = { amount ->
                    viewModel.handleEvent(AddTransactionEvent.OnAmountChanged(amount))
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
                        viewModel.handleEvent(AddTransactionEvent.OnDateSelected(date))
                    }
                    showDatePicker = false
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
                    viewModel.handleEvent(AddTransactionEvent.OnTimeSelected(formatted))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AddTransactionSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is AddTransactionSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
                }
            }
        }
    }
}

@Composable
fun TransactionScreenContent(
    modifier: Modifier = Modifier,
    date: String,
    time: String,
    accountName: String? = null,
    selectedCategory: String? = null,
    amount: String,
    currency: String? = null,
    comment: String? = null,
    onCategoryClicked: () -> Unit,
    onAmountClicked: () -> Unit,
    onDateClicked: () -> Unit,
    onTimeClicked: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = modifier) {
        ListItemComponent(
            title = stringResource(R.string.list_item_text_account),
            trailingText = accountName,
            trailingIcon = { TrailingIcon(R.drawable.ic_more_vert) },
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_category),
            trailingText = selectedCategory,
            trailingIcon = { TrailingIcon(R.drawable.ic_more_vert) },
            onClick = onCategoryClicked
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_sum),
            trailingText = "$amount ${currency?.toSymbol()}" ,
            onClick = onAmountClicked
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_date),
            trailingText = date,
            onClick = onDateClicked
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_time),
            trailingText = time,
            onClick = onTimeClicked
        )
        Divider()
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment ?: "",
            onValueChange = {
                onValueChange(it)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.comment_placeholder),
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
