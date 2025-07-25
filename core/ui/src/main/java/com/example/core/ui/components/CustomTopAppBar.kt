package com.example.core.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    @StringRes titleRes: Int,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null,
    onLeadingClick: (() -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1D1B20)
            )
        },
        navigationIcon = {
            if (leadingIconRes != null && onLeadingClick != null) {
                IconButton(onClick = onLeadingClick) {
                    Icon(
                        painter = painterResource(leadingIconRes),
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
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = Color(0xFF1D1B20),
            navigationIconContentColor = Color(0xFF1D1B20)
        )
    )
}
