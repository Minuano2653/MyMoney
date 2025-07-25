package com.example.mymoney.presentation.screens.main

import com.example.core.ui.contract.BaseUiState

data class MainUiState(
    val isNetworkAvailable: Boolean = true,
    val wasNetworkLost: Boolean = false
): BaseUiState