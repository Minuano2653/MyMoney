package com.example.mymoney.presentation.screens.categories

import com.example.mymoney.domain.entity.Category

data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
)