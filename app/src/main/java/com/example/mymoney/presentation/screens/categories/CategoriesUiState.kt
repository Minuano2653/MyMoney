package com.example.mymoney.presentation.screens.categories

import com.example.mymoney.domain.entity.Category

data class CategoriesUiState(
    val categories: List<Category> = emptyList()
)