package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

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