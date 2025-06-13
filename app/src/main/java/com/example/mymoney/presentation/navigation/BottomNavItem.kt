package com.example.mymoney.presentation.navigation

import com.example.mymoney.R

sealed class BottomNavItem(
    val screen: Screen,
    val label: String,
    val iconRes: Int
) {
    data object Expenses: BottomNavItem(
        Screen.Expenses,
        "Расходы",
        R.drawable.ic_expenses
    )

    data object Incomes: BottomNavItem(
        Screen.Incomes,
        "Доходы",
        R.drawable.ic_incomes
    )

    data object Account: BottomNavItem(
        Screen.Account,
        "Счет",
        R.drawable.ic_account
    )
    data object Categories: BottomNavItem(
        Screen.Categories,
        "Статьи",
        R.drawable.ic_categories
    )
    data object Settings: BottomNavItem(
        Screen.Settings,
        "Настройки",
        R.drawable.ic_settings
    )
}