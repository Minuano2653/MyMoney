package com.example.mymoney.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.components.Divider
import com.example.core.ui.components.ListItemComponent
import com.example.mymoney.R
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Composable
fun CurrencyBottomSheetContent(
    onCurrencyClick: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_ruble),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null,

                )
            },
            title = stringResource(R.string.ruble_lable),
            onClick = { onCurrencyClick(context.getString(R.string.ruble_code)) }
        )
        Divider()
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_dollar),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            },
            title = stringResource(R.string.dollar_lable),
            onClick = { onCurrencyClick(context.getString(R.string.dollar_code)) }
        )
        Divider()
        ListItemComponent(
            itemHeight = 72.dp,
            leadingIcon = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_euro),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            },
            title = stringResource(R.string.euro_lable),
            onClick = { onCurrencyClick(context.getString(R.string.euro_code)) }
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
            title = stringResource(R.string.dialog_negative_button),
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