package com.example.mymoney.presentation.screens.main

import com.example.mymoney.presentation.base.contract.BaseUiState

data class MainUiState(
    val isNetworkAvailable: Boolean = true,
    val hasShownNetworkError: Boolean = false
): BaseUiState