package com.example.mymoney.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen

/**
 * Навигационный граф приложения, объединяющий навигацию по основным экранам.
 *
 * @param navHostController Контроллер навигации.
 * @param modifier Модификатор для NavHost.
 */
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    ) {
    NavHost(
        navController = navHostController,
        startDestination = Expenses,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier
    ) {
        expensesNavGraph(
            onNavigateToHistory = {
                navHostController.navigate(TransactionsHistory(isIncome = false))
            },
            onNavigateToAddExpense = {
                navHostController.navigate(TransactionDetail(isIncome = false))
            },
            onNavigateToTransactionDetail = { transactionId ->
                navHostController.navigate(EditTransaction(isIncome = false, transactionId))
            },
            onNavigateToAnalysis = { isIncome ->
                navHostController.navigate(Analysis(isIncome))
            },
            onNavigateBack = {
                navHostController.popBackStack()
            }
        )

        incomesNavGraph(
            onNavigateToHistory = {
                navHostController.navigate(TransactionsHistory(isIncome = true))
            },
            onNavigateToAddIncome = {
                navHostController.navigate(TransactionDetail(isIncome = true))
            },
            onNavigateToTransactionDetail = { transactionId ->
                navHostController.navigate(EditTransaction(isIncome = true, transactionId))
            },
            onNavigateToAnalysis = { isIncome ->
                navHostController.navigate(Analysis(isIncome))
            },
            onNavigateBack = {
                navHostController.popBackStack()
            }
        )

        accountNavGraph(
            onNavigateToEditAccount = { accountId ->
                navHostController.navigate(EditAccount(accountId))
            },
            onNavigateBack = {
                navHostController.popBackStack()
            }
        )

        composable<Categories> {
            CategoriesScreen()
        }

        settingsNavGraph(
            onNavigateToAboutApp = {
                navHostController.navigate(AboutApp)
            },
            onNavigateToLanguage = {
                navHostController.navigate(Language)
            },
            onNavigateToPassword = {
                navHostController.navigate(Password)
            },
            onNavigateToHaptics = {
                navHostController.navigate(Haptics)
            },
            onNavigateToPrimaryColor = {
                navHostController.navigate(PrimaryColors)
            },
            onNavigateBack = {
                navHostController.popBackStack()
            },
        )
    }
}