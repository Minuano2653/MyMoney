package com.example.mymoney.presentation.screens.incomes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.components.EmojiIcon
import com.example.mymoney.presentation.components.TrailingIcon
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Income
import com.example.mymoney.presentation.components.ListItemComponent
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency

@Preview
@Composable
fun IncomesScreenPreview() {
    MyMoneyTheme {
        IncomesScreen(
            onUpdateTopAppBar = {},
            onUpdateFabState = {}
        )
    }
}

@Composable
fun IncomesScreen(
    modifier: Modifier = Modifier,
    onUpdateTopAppBar: (TopAppBarState) -> Unit,
    onUpdateFabState: (FabState) -> Unit
) {
    LaunchedEffect(Unit) {
        onUpdateTopAppBar(
            TopAppBarState(
                title = "Доходы сегодня",
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {  }
            )
        )
        onUpdateFabState(
            FabState(
                isVisible = true,
                onClick = {}
            )
        )
    }

    val uiState = getMockIncomesUiState()

    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            backgroundColor = MaterialTheme.colorScheme.secondary,
            itemHeight = 56.dp,
            title = "Всего",
            trailingText = uiState.total.toCurrency()
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(uiState.incomes) { _, incomes ->
                ListItemComponent(
                    title = incomes.category.name,
                    subtitle = incomes.comment,
                    trailingText = incomes.amount.toCurrency(),
                    leadingIcon = {
                        EmojiIcon(emoji = incomes.category.emoji)
                    },
                    trailingIcon = {
                        TrailingIcon()
                    }
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

private fun getMockIncomesUiState() = IncomesUiState(
    incomes = listOf(
        Income(
            id = 1,
            category = Category(1, "Зарплата", "\uD83D\uDCB0", isIncome = true),
            amount = 500000.00.toBigDecimal(),
            createdAt = "2025-01-01",
            updatedAt = "2025-01-01"
        ),
        Income(
            id = 2,
            category = Category(2, "Подработка", "\uD83D\uDCB0", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        )
    )
)