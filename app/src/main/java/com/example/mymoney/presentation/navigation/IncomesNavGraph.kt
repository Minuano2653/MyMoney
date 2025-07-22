package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.mymoney.presentation.screens.add_transaction.AddTransactionScreen
import com.example.mymoney.presentation.screens.analysis.AnalysisScreen
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionScreen
import com.example.mymoney.presentation.screens.history.HistoryScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen

/**
 * Добавляет вложенный граф навигации для раздела "Доходы" в основной [NavGraphBuilder].
 */
fun NavGraphBuilder.incomesNavGraph(
    onNavigateToHistory: () -> Unit,
    onNavigateToAddIncome: () -> Unit,
    onNavigateToTransactionDetail: (Int) -> Unit,
    onNavigateToAnalysis: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Incomes>(
        startDestination = IncomesToday
    ) {
        composable<IncomesToday> {
            IncomesScreen(
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToAddIncome = onNavigateToAddIncome,
                onNavigateToTransactionDetail = onNavigateToTransactionDetail,
                modifier = modifier
            )
        }

        composable<TransactionsHistory> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsHistory>()
            HistoryScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToEditTransaction = onNavigateToTransactionDetail,
                onNavigateToAnalysis = onNavigateToAnalysis,
                modifier = modifier
            )
        }

        composable<TransactionDetail> { backStackEntry ->
            AddTransactionScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }

        composable<EditTransaction> { backStackEntry ->
            EditTransactionScreen(
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