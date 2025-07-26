package com.example.mymoney.presentation.screens.settings.theme

import com.example.core.ui.contract.BaseUiState
import com.example.mymoney.presentation.theme.AppTheme

data class ThemeUiState(
    val currentTheme: AppTheme = AppTheme(),
    val isLoading: Boolean = false
): BaseUiState
