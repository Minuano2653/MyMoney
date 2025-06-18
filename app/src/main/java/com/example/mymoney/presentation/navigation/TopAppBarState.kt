package com.example.mymoney.presentation.navigation

data class TopAppBarState(
    val title: String = "",
    val leadingIconRes: Int? = null,
    val trailingIconRes: Int? = null,
    val onLeadingClick: (() -> Unit)? = null,
    val onTrailingClick: (() -> Unit)? = null,
)