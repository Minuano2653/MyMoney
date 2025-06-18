package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen

@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    expensesScreenContent: @Composable () -> Unit,
    incomesScreenContent: @Composable () -> Unit,
    accountScreenContent: @Composable () -> Unit,
    categoriesScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        composable(Screen.Expenses.route) {
            expensesScreenContent()
        }
        composable(Screen.Incomes.route) {
            incomesScreenContent()
        }
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