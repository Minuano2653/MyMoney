package com.example.mymoney.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.ui.theme.MyMoneyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(state: TopAppBarState) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = state.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (state.leadingIconRes != null && state.onLeadingClick != null) {
                IconButton(onClick = state.onLeadingClick) {
                    Icon(
                        painter = painterResource(state.leadingIconRes),
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (state.trailingIconRes != null && state.onTrailingClick != null) {
                IconButton(onClick = state.onTrailingClick) {
                    Icon(
                        painter = painterResource(state.trailingIconRes),
                        contentDescription = null
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
            TopAppBarState(
                title = "Расходы сегодня",
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {}
            )
        )
    }
}