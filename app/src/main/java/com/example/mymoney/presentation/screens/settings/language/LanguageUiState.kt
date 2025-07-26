package com.example.mymoney.presentation.screens.settings.language

import com.example.core.domain.entity.AppLanguage
import com.example.core.ui.contract.BaseUiState

data class LanguageUiState(
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isLoading: Boolean = false,
    val error: String? = null
) : BaseUiState