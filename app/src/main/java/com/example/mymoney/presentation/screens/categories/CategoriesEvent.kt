package com.example.mymoney.presentation.screens.categories

import com.example.mymoney.presentation.base.contract.BaseEvent

/**
 * События, связанные с экраном статей.
 * Используются для обработки действий пользователя во ViewModel.
 */
sealed class CategoriesEvent: BaseEvent {
    object LoadCategories : CategoriesEvent()
    object OnCategoryClicked: CategoriesEvent()
    data class OnSearchQueryChanged(val query: String): CategoriesEvent()
}
