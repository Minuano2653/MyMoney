package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * Навигационный граф приложения, объединяющий навигацию по основным экранам.
 *
 * @param navHostController Контроллер навигации.
 * @param modifier Модификатор для NavHost.
 * @param expensesTodayScreenContent composable контент для экрана "Расходы сегодня".
 * @param expensesHistoryScreenContent composable контент для экрана "История расходов".
 * @param incomesTodayScreenContent composable контент для экрана "Доходы сегодня".
 * @param incomesHistoryScreenContent composable контент для экрана "История доходов".
 * @param accountScreenContent composable контент для экрана "Аккаунт".
 * @param categoriesScreenContent composable контент для экрана "Категории".
 * @param settingsScreenContent composable контент для экрана "Настройки".
 */
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
