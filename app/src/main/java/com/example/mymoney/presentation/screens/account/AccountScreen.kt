package com.example.mymoney.presentation.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymoney.presentation.screens.EmojiIcon
import androidx.compose.ui.unit.dp
import com.example.mymoney.presentation.screens.ListItemComponent
import com.example.mymoney.presentation.screens.TrailingIcon
import com.example.mymoney.domain.entity.Account
import com.example.mymoney.ui.theme.MyMoneyTheme
import com.example.mymoney.utils.toCurrency
import com.example.mymoney.utils.toSymbol

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    MyMoneyTheme {
        AccountScreen()
    }
}

@Composable
fun AccountScreen(modifier: Modifier = Modifier) {
    val uiState = getMockAccount()

    Column(modifier = modifier.fillMaxSize()) {
        ListItemComponent(
            title = uiState.account.name,
            trailingText = uiState.account.balance.toCurrency(),
            leadingIcon = {
                EmojiIcon(
                    "\uD83D\uDCB0",
                    backgroundColor = MaterialTheme.colorScheme.onSecondary
                )
            },
            trailingIcon = {
                TrailingIcon()
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ListItemComponent(
            title = "Валюта",
            trailingText = uiState.account.currency.toSymbol(),

            trailingIcon = {
                TrailingIcon()
            },
            itemHeight = 56.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
        )
    }
}

private fun getMockAccount(): AccountUiState = AccountUiState(
    Account(1, "Баланс", (-670000.00).toBigDecimal(), "RUB")
)