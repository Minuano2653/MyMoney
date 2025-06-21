package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    expensesTodayScreenContent: @Composable () -> Unit,
    expensesHistoryScreenContent: @Composable () -> Unit,
    incomesTodayScreenContent: @Composable () -> Unit,
    incomesHistoryScreenContent: @Composable () -> Unit,
    accountScreenContent: @Composable () -> Unit,
    categoriesScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        expensesNavGraph(
            expensesTodayScreenContent = expensesTodayScreenContent,
            expensesHistoryScreenContent = expensesHistoryScreenContent
        )
        incomesNavGraph(
            incomesTodayScreenContent = incomesTodayScreenContent,
            incomesHistoryScreenContent = incomesHistoryScreenContent
        )
        composable(Screen.Account.route) {
            accountScreenContent()
        }
        composable(Screen.Categories.route) {
            categoriesScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
    }
}