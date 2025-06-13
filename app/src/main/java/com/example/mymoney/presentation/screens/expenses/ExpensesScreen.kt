package com.example.mymoney.presentation.screens.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymoney.EmojiIcon
import com.example.mymoney.ListItemComponent
import com.example.mymoney.TrailingIcon
import com.example.mymoney.domain.entity.Category
import com.example.mymoney.domain.entity.Expense
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency


@Preview
@Composable
fun ExpensesScreenPreview() {
    MyMoneyTheme {
        ExpensesScreen()
    }
}

@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    val uiState = getMockExpensesUiState()
    Column(modifier = Modifier.fillMaxSize()) {
        ListItemComponent(
            title = "Всего",
            trailText = uiState.total.toCurrency(),
            backgroundColor = MaterialTheme.colorScheme.secondary,
            itemHeight = 56.dp
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            itemsIndexed(uiState.expenses) { _, expense ->
                ListItemComponent(
                    title = expense.category.name,
                    subtitle = expense.comment,
                    trailText = expense.amount.toCurrency(),
                    leadingIcon = {
                        EmojiIcon(emoji = expense.category.emoji)
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

private fun getMockExpensesUiState() = ExpensesUiState(
    expenses = listOf(
        Expense(
            id = 1,
            category = Category(1, "Аренда квартиры", "🏡", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-01",
            updatedAt = "2025-01-01"
        ),
        Expense(
            id = 2,
            category = Category(2, "Одежда", "👗", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 3,
            category = Category(3, "На собачку", "🐶", isIncome = false),
            comment = "Джек",
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 4,
            category = Category(3, "На собачку", "🐶", isIncome = false),
            comment = "Энни",
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 5,
            category = Category(4, "Ремонт квартиры", "РК", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 6,
            category = Category(5, "Продукты", "🍭", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 7,
            category = Category(6, "Спортзал", "🏋️", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        ),
        Expense(
            id = 8,
            category = Category(7, "Медицина", "💊", isIncome = false),
            amount = 100000.00.toBigDecimal(),
            createdAt = "2025-01-02",
            updatedAt = "2025-01-02"
        )
    )
)