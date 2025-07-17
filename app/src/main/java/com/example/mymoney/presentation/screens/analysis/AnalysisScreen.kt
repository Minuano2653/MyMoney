package com.example.mymoney.presentation.screens.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymoney.R
import com.example.mymoney.presentation.base.viewmodel.daggerViewModel
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.components.DateChip
import com.example.mymoney.presentation.components.DatePickerModal
import com.example.mymoney.presentation.components.Divider
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.EmptyContent
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.components.LoadingCircularIndicator
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.presentation.screens.analysis.model.CategoryAnalysis
import com.example.mymoney.presentation.theme.MyMoneyTheme
import com.example.mymoney.utils.DateUtils
import com.example.mymoney.utils.DateUtils.formatDateWithMonthInGenitive
import com.example.mymoney.utils.formatAmount
import com.example.mymoney.utils.toSymbol
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AnalysisViewModel = daggerViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            CustomTopAppBar(
                titleRes = R.string.top_bar_title_analysis,
                leadingIconRes = R.drawable.ic_arrow_back,
                trailingIconRes = R.drawable.ic_analysis,
                onLeadingClick = {
                    viewModel.handleEvent(AnalysisEvent.OnBackPressed)
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        AnalysisScreenContent(
            isLoading = uiState.isLoading,
            categoryAnalysis = uiState.categoryAnalysis,
            total = uiState.total,
            currency = uiState.currency,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            onStartDateClick = { viewModel.handleEvent(AnalysisEvent.OnStartDateClicked) },
            onEndDateClick = { viewModel.handleEvent(AnalysisEvent.OnEndDateClicked) },
            modifier = modifier.padding(paddingValues),

        )

        if (uiState.showStartDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.startDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(AnalysisEvent.OnStartDateSelected(date))
                    }
                },
                onDismiss = { viewModel.handleEvent(AnalysisEvent.OnStartDateClicked) }
            )
        }

        if (uiState.showEndDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.endDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(AnalysisEvent.OnEndDateSelected(date))
                    }
                },
                onDismiss = { viewModel.handleEvent(AnalysisEvent.OnEndDateClicked) }
            )
        }

    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AnalysisSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is AnalysisSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }
}

@Composable
fun AnalysisScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    categoryAnalysis: List<CategoryAnalysis>,
    total: BigDecimal,
    currency: String,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        AnalysisFilterSection(
            startDate = startDate,
            endDate = endDate,
            total = total,
            currency = currency,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick
        )
        when {
            isLoading -> {
                LoadingCircularIndicator(modifier = Modifier.fillMaxSize())
            }
            categoryAnalysis.isEmpty() -> {
                EmptyContent(
                    noContentLabel = R.string.no_analysis_data,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                AnalysisContent(
                    categoryAnalysisList = categoryAnalysis,
                    currency = currency,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AnalysisFilterSection(
    startDate: String,
    endDate: String,
    total: BigDecimal,
    currency: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItemComponent(
            title = stringResource(R.string.list_item_text_period_start),
            trailingIcon = {
                DateChip(
                    date = formatDateWithMonthInGenitive(startDate),
                    onClick = onStartDateClick
                )
            }
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_period_end),
            trailingIcon = {
                DateChip(
                    date = formatDateWithMonthInGenitive(endDate),
                    onClick = onEndDateClick
                )
            }
        )
        Divider()
        ListItemComponent(
            title = stringResource(R.string.list_item_text_sum),
            trailingText = "${total.formatAmount()} ${currency.toSymbol()}",
        )
        Divider()
    }
}

@Composable
fun AnalysisContent(
    categoryAnalysisList: List<CategoryAnalysis>,
    currency: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = categoryAnalysisList,
            key = { _, categoryAnalysis -> categoryAnalysis.categoryId }
        ) { _, categoryAnalysis ->
            ListItemComponent(
                leadingIcon = { EmojiIcon(emoji = categoryAnalysis.categoryEmoji) },
                trailingIcon = { TrailingIcon() },
                title = categoryAnalysis.categoryName,
                trailingText = categoryAnalysis.percentage,
                trailingSubText = "${categoryAnalysis.totalAmount} ${currency.toSymbol()}",
            )
            Divider()
        }
    }
}