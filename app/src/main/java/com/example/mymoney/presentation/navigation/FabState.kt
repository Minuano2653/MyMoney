package com.example.mymoney.presentation.navigation

import com.example.mymoney.R

data class FabState(
    val isVisible: Boolean = false,
    val iconRes: Int = R.drawable.ic_add,
    val onClick: (() -> Unit)? = null
)