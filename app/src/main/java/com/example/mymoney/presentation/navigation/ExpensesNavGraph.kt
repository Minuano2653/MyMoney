package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.toRoute

/**
 * Добавляет вложенный граф навигации для раздела "Расходы" в основной [NavGraphBuilder].
 *
 * @param expensesTodayScreenContent composable контент для экрана "Расходы сегодня".
 * @param expensesHistoryScreenContent composable контент для экрана "История расходов".
 */
fun NavGraphBuilder.expensesNavGraph(
    expensesTodayScreenContent: @Composable () -> Unit,
    expensesHistoryScreenContent: @Composable (Boolean) -> Unit,
) {
    navigation<Expenses>(
        startDestination = ExpensesToday
    ) {
        composable<ExpensesToday> {
            expensesTodayScreenContent()
        }
        composable<TransactionsHistory> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsHistory>()
            expensesHistoryScreenContent(args.isIncome)
        }
    }
}
