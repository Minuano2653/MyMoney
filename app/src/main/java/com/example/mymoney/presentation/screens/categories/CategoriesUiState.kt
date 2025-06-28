package com.example.mymoney.presentation.screens.categories

import com.example.mymoney.domain.entity.Category
import com.example.mymoney.presentation.base.contract.BaseUiState

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
    val error: String? = null,
    val isNetworkAvailable: Boolean = true
): BaseUiState
