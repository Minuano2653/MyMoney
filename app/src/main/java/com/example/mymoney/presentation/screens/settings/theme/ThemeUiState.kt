package com.example.mymoney.presentation.screens.settings.theme

import com.example.core.ui.contract.BaseUiState

data class ThemeUiState(
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
) : BaseUiState