package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

/**
 * Добавляет вложенный граф навигации для раздела "Расходы" в основной [NavGraphBuilder].
 *
 * @param expensesTodayScreenContent composable контент для экрана "Расходы сегодня".
 * @param expensesHistoryScreenContent composable контент для экрана "История расходов".
 */
fun NavGraphBuilder.expensesNavGraph(
    expensesTodayScreenContent: @Composable () -> Unit,
    expensesHistoryScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.ExpensesToday.route,
        route = Screen.Expenses.route
    ) {
        composable(Screen.ExpensesToday.route) {
            expensesTodayScreenContent()
        }
        composable(Screen.ExpensesHistory.route) {
            expensesHistoryScreenContent()
        }
    }
}
