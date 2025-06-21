package com.example.mymoney.presentation.screens.categories

sealed class CategoriesEvent {
    object LoadCategories : CategoriesEvent()
    object OnCategoryClicked: CategoriesEvent()
}
