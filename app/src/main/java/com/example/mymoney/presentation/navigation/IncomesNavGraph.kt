package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute

/**
 * Добавляет вложенный граф навигации для раздела "Доходы" в основной [NavGraphBuilder].
 *
 * @param incomesTodayScreenContent composable контент для экрана "Доходы сегодня".
 * @param incomesHistoryScreenContent composable контент для экрана "История доходов".
 */
fun NavGraphBuilder.incomesNavGraph(
    incomesTodayScreenContent: @Composable () -> Unit,
    incomesHistoryScreenContent: @Composable (Boolean) -> Unit,
) {
    navigation<Incomes>(
        startDestination = IncomesToday
    ) {
        composable<IncomesToday> {
            incomesTodayScreenContent()
        }
        composable<TransactionsHistory> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsHistory>()
            incomesHistoryScreenContent(args.isIncome)
        }
    }
}
