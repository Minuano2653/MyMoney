package com.example.mymoney.presentation.navigation


sealed class Screen(
    val route: String,
) {
    object Expenses: Screen(
        route = EXPENSES_ROUTE,
    )
    object Incomes: Screen(
        route = INCOMES_ROUTE,
    )
    object Account: Screen(
        route = ACCOUNT_ROUTE,
    )
    object Categories: Screen(
        route = CATEGORIES_ROUTE,
    )
    object Settings: Screen(
        route = SETTINGS_ROUTE,
    )

    companion object {
        const val EXPENSES_ROUTE = "Expenses"
        const val INCOMES_ROUTE = "Incomes"
        const val ACCOUNT_ROUTE = "Account"
        const val CATEGORIES_ROUTE = "Categories"
        const val SETTINGS_ROUTE = "Settings"
    }
}