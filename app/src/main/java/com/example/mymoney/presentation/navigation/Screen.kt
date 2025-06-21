package com.example.mymoney.presentation.navigation


sealed class Screen(
    val route: String,
) {
    object Expenses: Screen(
        route = ROUTE_EXPENSES,
    )
    object ExpensesHistory: Screen(
        route = ROUTE_EXPENSES_HISTORY
    )
    object ExpensesToday: Screen(
        route = ROUTE_EXPENSES_TODAY
    )
    object Incomes: Screen(
        route = ROUTE_INCOMES,
    )
    object IncomesHistory: Screen(
        route = ROUTE_INCOMES_HISTORY
    )
    object IncomesToday: Screen(
        route = ROUTE_INCOMES_TODAY
    )
    object Account: Screen(
        route = ROUTE_ACCOUNT,
    )
    object Categories: Screen(
        route = ROUTE_CATEGORIES,
    )
    object Settings: Screen(
        route = ROUTE_SETTINGS,
    )


    companion object {
        const val ROUTE_EXPENSES = "Expenses"
        const val ROUTE_INCOMES = "Incomes"
        const val ROUTE_ACCOUNT = "Account"
        const val ROUTE_CATEGORIES = "Categories"
        const val ROUTE_SETTINGS = "Settings"
        const val ROUTE_EXPENSES_HISTORY = "ExpensesHistory"
        const val ROUTE_EXPENSES_TODAY = "ExpensesToday"
        const val ROUTE_INCOMES_HISTORY = "IncomesHistory"
        const val ROUTE_INCOMES_TODAY = "IncomesToday"
    }
}