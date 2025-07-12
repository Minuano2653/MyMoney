package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    addExpenseScreenContent: @Composable (Boolean) -> Unit,
    editExpenseScreenContent: @Composable (Boolean, Int) -> Unit,
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
        composable<TransactionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionDetail>()
            addExpenseScreenContent(args.isIncome)
        }
        composable<EditTransaction> { backStackEntry ->
            val args = backStackEntry.toRoute<EditTransaction>()
            editExpenseScreenContent(args.isIncome, args.transactionId)
        }
    }
}
