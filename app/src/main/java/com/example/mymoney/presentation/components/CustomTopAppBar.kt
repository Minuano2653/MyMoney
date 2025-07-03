package com.example.mymoney.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymoney.R
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    @StringRes titleRes: Int = R.string.top_bar_title_expenses,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null,
    onLeadingClick: (() -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (leadingIconRes != null && onLeadingClick != null) {
                IconButton(onClick = onLeadingClick) {
                    Icon(
                        painter = painterResource(leadingIconRes),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (trailingIconRes != null && onTrailingClick != null) {
                IconButton(onClick = onTrailingClick) {
                    Icon(
                        painter = painterResource(trailingIconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Preview
@Composable
fun MyTopAppBarPreview() {
    MyMoneyTheme {
        CustomTopAppBar(
            titleRes = R.string.top_bar_title_expenses,
            trailingIconRes = R.drawable.ic_history,
            onTrailingClick = {}
        )
    }
}
