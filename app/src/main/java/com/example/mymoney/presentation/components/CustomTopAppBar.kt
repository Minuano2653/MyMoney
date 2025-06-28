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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymoney.R
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.theme.MyMoneyTheme

/**
 * Компонент верхнего AppBar с центрированным заголовком и опциональными иконками слева и справа.
 *
 * @param state Состояние AppBar, содержащее ресурсы заголовка, иконок и обработчики кликов.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(state: TopAppBarState) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(state.titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (state.leadingIconRes != null && state.onLeadingClick != null) {
                IconButton(onClick = state.onLeadingClick) {
                    Icon(
                        painter = painterResource(state.leadingIconRes),
                        tint = MaterialTheme.colorScheme.onSurface,
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
            TopAppBarState(
                titleRes = R.string.top_bar_title_expenses,
                trailingIconRes = R.drawable.ic_history,
                onTrailingClick = {}
            )
        )
    }
}
