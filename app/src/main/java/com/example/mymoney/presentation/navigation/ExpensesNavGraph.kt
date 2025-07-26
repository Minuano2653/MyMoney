package com.example.mymoney.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.mymoney.presentation.screens.add_transaction.AddTransactionScreen
import com.example.mymoney.presentation.screens.analysis.AnalysisScreen
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.history.HistoryScreen

/**
 * Добавляет вложенный граф навигации для раздела "Расходы" в основной [NavGraphBuilder].
 */
fun NavGraphBuilder.expensesNavGraph(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddExpense: () -> Unit,
    onNavigateToTransactionDetail: (Int) -> Unit,
    onNavigateToAnalysis: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Expenses>(
        startDestination = ExpensesToday
    ) {
        composable<ExpensesToday> {
            ExpensesScreen(
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToAddExpense = onNavigateToAddExpense,
                onNavigateToTransactionDetail = onNavigateToTransactionDetail,
                modifier = modifier
            )
        }

        composable<TransactionsHistory> { backStackEntry ->
            HistoryScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToEditTransaction = onNavigateToTransactionDetail,
                onNavigateToAnalysis = onNavigateToAnalysis,
                modifier = modifier
            )
        }

        composable<TransactionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionDetail>()
            AddTransactionScreen(
                isIncome = args.isIncome,
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        composable<EditTransaction> { backStackEntry ->
            val args = backStackEntry.toRoute<EditTransaction>()
            EditTransactionScreen(
                isIncome = args.isIncome,
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        composable<Analysis> { backStackEntry ->
            AnalysisScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
    }
}
