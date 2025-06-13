package com.example.mymoney.presentation.navigation

import com.example.mymoney.R

sealed class Screen(
    val route: String,
    val title: String,
    val leadingIconRes: Int? = null,
    val trailingIconRes: Int? = null,
    val onLeadingClick: (() -> Unit)? = null,
    val onTrailingClick: (() -> Unit)? = null,
    val hasFloatingActionButton: Boolean
) {
    object Expenses: Screen(
        route = EXPENSES_ROUTE,
        title = EXPENSES_TITLE,
        trailingIconRes = R.drawable.ic_history,
        hasFloatingActionButton = true
    )
    object Incomes: Screen(
        route = INCOMES_ROUTE,
        title = INCOMES_TITLE,
        trailingIconRes = R.drawable.ic_history,
        hasFloatingActionButton = true
    )
    object Account: Screen(
        route = ACCOUNT_ROUTE,
        title = ACCOUNT_TITLE,
        trailingIconRes = R.drawable.ic_edit,
        hasFloatingActionButton = true
    )
    object Categories: Screen(
        route = CATEGORIES_ROUTE,
        title = CATEGORIES_TITLE,
        hasFloatingActionButton = false
    )
    object Settings: Screen(
        route = SETTINGS_ROUTE,
        title = OPTIONS_TITLE,
        hasFloatingActionButton = false
    )

    companion object {

        const val EXPENSES_ROUTE = "Expenses"
        const val INCOMES_ROUTE = "Incomes"
        const val ACCOUNT_ROUTE = "Account"
        const val CATEGORIES_ROUTE = "Categories"
        const val SETTINGS_ROUTE = "Settings"

        const val EXPENSES_TITLE = "Расходы сегодня"
        const val INCOMES_TITLE = "Доходы сегодня"
        const val ACCOUNT_TITLE = "Мой счет"
        const val CATEGORIES_TITLE = "Мои статьи"
        const val OPTIONS_TITLE = "Настройки"
    }

}