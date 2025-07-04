package com.example.mymoney.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymoney.R
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Composable
fun CurrencyBottomSheetContent(
    onCurrencyClick: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_ruble),
                    contentDescription = null
                )
            },
            title = "Российский рубль ₽",
            onClick = { onCurrencyClick("RUB") }
        )
        Divider()
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_dollar),
                    contentDescription = null
                )
            },
            title = "Американский доллар $",
            onClick = { onCurrencyClick("USD") }
        )
        Divider()
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_euro),
                    contentDescription = null
                )
            },
            title = "Евро €",
            onClick = { onCurrencyClick("EUR") }
        )
        Divider()
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_circle_cancel),
                    contentDescription = null
                )
            },
            title = "Отмена",
            onClick = onCancelClick,
            contentColor = Color.White,
            backgroundColor = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    MyMoneyTheme {
        CurrencyBottomSheetContent(
            onCurrencyClick = {},
            onCancelClick = {}
        )
    }
}