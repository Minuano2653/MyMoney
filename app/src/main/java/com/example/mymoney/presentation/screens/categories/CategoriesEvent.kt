package com.example.mymoney.presentation.screens.categories

import com.example.core.ui.contract.BaseEvent

/**
 * События, связанные с экраном статей.
 * Используются для обработки действий пользователя во ViewModel.
 */
sealed class CategoriesEvent: BaseEvent {
    object LoadCategories : CategoriesEvent()
    data class OnSearchQueryChanged(val query: String): CategoriesEvent()
}
