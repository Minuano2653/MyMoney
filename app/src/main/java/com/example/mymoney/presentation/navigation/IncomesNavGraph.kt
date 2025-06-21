package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

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
        composable(Screen.IncomesHistory.route) {
            incomesHistoryScreenContent()
        }
    }
}