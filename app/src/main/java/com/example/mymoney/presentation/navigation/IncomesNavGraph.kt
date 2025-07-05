package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

/**
 * Добавляет вложенный граф навигации для раздела "Доходы" в основной [NavGraphBuilder].
 *
 * @param incomesTodayScreenContent composable контент для экрана "Доходы сегодня".
 * @param incomesHistoryScreenContent composable контент для экрана "История доходов".
 */
fun NavGraphBuilder.incomesNavGraph(
    incomesTodayScreenContent: @Composable () -> Unit,
    incomesHistoryScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.IncomesToday.route,
        route = Screen.Incomes.route
    ) {
        composable(Screen.IncomesToday.route) {
            incomesTodayScreenContent()
        }
        composable(
            route = "${Screen.IncomesHistory.route}/{${Screen.ARGUMENT_HISTORY}}",
            arguments = listOf(
                navArgument(Screen.ARGUMENT_HISTORY) { type = NavType.BoolType }
            )
        ) {
            incomesHistoryScreenContent()
        }
    }
}
