package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        composable(
            route = Screen.ExpensesToday.route,
        ) {
            expensesTodayScreenContent()
        }
        composable(
            route = "${Screen.ExpensesHistory.route}/{${Screen.ARGUMENT_HISTORY}}",
            arguments = listOf(
                navArgument(Screen.ARGUMENT_HISTORY) { type = NavType.BoolType }
            )
        ) {
            expensesHistoryScreenContent()
        }
    }
}
