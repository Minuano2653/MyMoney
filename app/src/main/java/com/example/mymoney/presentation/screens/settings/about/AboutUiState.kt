package com.example.mymoney.presentation.screens.settings.about

import com.example.core.ui.contract.BaseUiState

data class AboutUiState(
    val appName: String = "",
    val appVersion: String = "",
    val lastUpdateDate: String = "",
    val lastUpdateTime: String = "",
    val isLoading: Boolean = false,
    val isNetworkAvailable: Boolean = true,
    val error: String? = null
): BaseUiState