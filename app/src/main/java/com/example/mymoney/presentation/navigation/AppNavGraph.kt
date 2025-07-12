package com.example.mymoney.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
 * @param accountInfoScreenContent composable контент для экрана "Аккаунт".
 * @param categoriesScreenContent composable контент для экрана "Категории".
 * @param settingsScreenContent composable контент для экрана "Настройки".
 */
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    expensesTodayScreenContent: @Composable () -> Unit,
    expensesHistoryScreenContent: @Composable (Boolean) -> Unit,
    incomesTodayScreenContent: @Composable () -> Unit,
    incomesHistoryScreenContent: @Composable (Boolean) -> Unit,
    accountInfoScreenContent: @Composable () -> Unit,
    editAccountScreenContent: @Composable (Int) -> Unit,
    categoriesScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    addExpenseScreenContent: @Composable (Boolean) -> Unit,
    addIncomeScreenContent: @Composable (Boolean) -> Unit,
    editExpenseScreenContent: @Composable (Boolean, Int) -> Unit,
    editIncomeScreenContent: @Composable (Boolean, Int) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Expenses,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        expensesNavGraph(
            expensesTodayScreenContent = expensesTodayScreenContent,
            expensesHistoryScreenContent = expensesHistoryScreenContent,
            addExpenseScreenContent = addExpenseScreenContent,
            editExpenseScreenContent = editExpenseScreenContent
        )

        incomesNavGraph(
            incomesTodayScreenContent = incomesTodayScreenContent,
            incomesHistoryScreenContent = incomesHistoryScreenContent,
            addIncomeScreenContent = addIncomeScreenContent,
            editIncomeScreenContent = editIncomeScreenContent
        )

        accountNavGraph(
            accountInfoScreenContent = accountInfoScreenContent,
            editAccountScreenContent = editAccountScreenContent
        )

        composable<Categories> {
            categoriesScreenContent()
        }

        composable<Settings> {
            settingsScreenContent()
        }
    }
}
