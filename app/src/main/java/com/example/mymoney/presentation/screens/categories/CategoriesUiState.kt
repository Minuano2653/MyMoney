package com.example.mymoney.presentation.screens.categories

import com.example.core.domain.entity.Category
import com.example.core.ui.contract.BaseUiState

/**
 * Состояние UI для экрана статей.
 *
 * @property isLoading Флаг загрузки данных.
 * @property categories Список статей.
 * @property error Сообщение об ошибке (если есть).
 * @property isNetworkAvailable Флаг доступности сети.
 */
data class CategoriesUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val error: Int? = null,
    val isNetworkAvailable: Boolean = true
): BaseUiState
