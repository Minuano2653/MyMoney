package com.example.mymoney.presentation.screens.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.common.utils.DateUtils
import com.example.core.common.utils.DateUtils.formatDateWithMonthInGenitive
import com.example.core.common.utils.formatAmount
import com.example.core.common.utils.toSymbol
import com.example.core.domain.entity.CategoryAnalysis
import com.example.core.ui.charts.pie.PieChart
import com.example.core.ui.charts.pie.PieChartItem
import com.example.core.ui.components.CustomTopAppBar
import com.example.core.ui.components.DateChip
import com.example.core.ui.components.DatePickerModal
import com.example.core.ui.components.Divider
import com.example.core.ui.components.EmojiIcon
import com.example.core.ui.components.EmptyContent
import com.example.core.ui.components.ListItemComponent
import com.example.core.ui.components.LoadingCircularIndicator
import com.example.core.ui.components.TrailingIcon
import com.example.mymoney.R
import com.example.mymoney.presentation.daggerViewModel
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AnalysisViewModel = daggerViewModel()
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
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
            onStartDateClick = { showStartDatePicker = true },
            onEndDateClick = { showEndDatePicker = true },
            modifier = modifier.padding(paddingValues),

        )

        if (showStartDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.startDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(AnalysisEvent.OnStartDateSelected(date))
                    }
                    showStartDatePicker = false
                },
                onDismiss = { showStartDatePicker = false }
            )
        }

        if (showEndDatePicker) {
            DatePickerModal(
                initialSelectedDateMillis = DateUtils.toMillis(uiState.endDate),
                onDateSelected = { millis ->
                    millis?.let {
                        val date = DateUtils.formatDateFromMillis(it)
                        viewModel.handleEvent(AnalysisEvent.OnEndDateSelected(date))
                    }
                    showEndDatePicker = false
                },
                onDismiss = { showEndDatePicker = false }
            )
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AnalysisSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(context.getString(effect.message))
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PieChart(
            items = categoryAnalysisList.map {
                PieChartItem(
                    label = it.categoryName,
                    value = it.totalAmount.toFloat()
                )
            },
            size = 250.dp,
            showInnerLegend = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        categoryAnalysisList.forEachIndexed { index, categoryAnalysis ->
            Divider()
            ListItemComponent(
                leadingIcon = { EmojiIcon(emoji = categoryAnalysis.categoryEmoji) },
                trailingIcon = { TrailingIcon(R.drawable.ic_more_vert) },
                title = categoryAnalysis.categoryName,
                trailingText = "${categoryAnalysis.percentage}%",
                trailingSubText = "${categoryAnalysis.totalAmount.formatAmount()} ${currency.toSymbol()}",
            )
            if (index == categoryAnalysisList.size - 1) {
                Divider()
            }
        }
    }
}
