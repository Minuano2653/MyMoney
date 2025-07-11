package com.example.mymoney.presentation.screens.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.Account
import com.example.mymoney.presentation.navigation.TransactionDetail
import com.example.mymoney.presentation.navigation.AppNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.navigation.Categories
import com.example.mymoney.presentation.navigation.EditTransaction
import com.example.mymoney.presentation.navigation.Expenses
import com.example.mymoney.presentation.navigation.Incomes
import com.example.mymoney.presentation.navigation.NavigationState
import com.example.mymoney.presentation.navigation.Settings
import com.example.mymoney.presentation.navigation.TransactionsHistory
import com.example.mymoney.presentation.navigation.rememberNavigationState
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.edit_account.EditAccountScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.history.HistoryScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.add_transaction.AddTransactionScreen
import com.example.mymoney.presentation.screens.edit_transaction.EditTransactionScreen
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navigationState: NavigationState = rememberNavigationState()
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val navigationList = listOf(
                BottomNavItem(Expenses, R.string.bottom_label_expenses, R.drawable.ic_expenses),
                BottomNavItem(Incomes, R.string.bottom_label_incomes, R.drawable.ic_incomes),
                BottomNavItem(Account, R.string.bottom_label_account, R.drawable.ic_account),
                BottomNavItem(Categories, R.string.bottom_label_categories, R.drawable.ic_categories),
                BottomNavItem(Settings, R.string.bottom_label_settings, R.drawable.ic_settings)
            )

            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                navigationList.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navigationState.navigateTo(item.route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.labelRes),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selected) FontWeight.W600 else FontWeight.Medium
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AppNavGraph(
            modifier = modifier.padding(paddingValues),
            navHostController = navigationState.navHostController,
            expensesTodayScreenContent = {
                ExpensesScreen(
                    onNavigateToHistory = {
                        navigationState.navHostController.navigate(TransactionsHistory(false))
                    },
                    onNavigateToAddExpense = { navigationState.navHostController.navigate(TransactionDetail(false)) },
                    onNavigateToTransactionDetail = { transactionId ->
                        navigationState.navHostController.navigate(
                            EditTransaction(false, transactionId)
                        )
                    },
                    modifier = modifier
                )
            },
            incomesTodayScreenContent = {
                IncomesScreen(
                    onNavigateToHistory = {
                        navigationState.navHostController.navigate(TransactionsHistory(true))
                    },
                    onNavigateToAddIncome = { navigationState.navHostController.navigate(TransactionDetail(true)) },
                    onNavigateToTransactionDetail = { transactionId ->
                        navigationState.navHostController.navigate(
                            EditTransaction(true, transactionId)
                        )
                    },
                    modifier = modifier
                )
            },
            accountInfoScreenContent = {
                AccountScreen(
                    onNavigateToEditAccount = { accountId ->
                        navigationState.navigateToEditAccount(accountId = accountId)
                    },
                    modifier = modifier
                )
            },
            categoriesScreenContent = {
                CategoriesScreen(
                    onNavigateToCategoriesDetail = { /*navigationState.navigateTo("Навигация на категорию")*/ },
                    modifier = modifier
                )
            },
            settingsScreenContent = {
                SettingsScreen(
                    modifier = modifier
                )
            },
            expensesHistoryScreenContent = {
                HistoryScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    onNavigateToEditTransaction = {transactionId ->
                        navigationState.navHostController.navigate(EditTransaction(false, transactionId))
                    },
                    modifier = modifier
                )
            },
            incomesHistoryScreenContent = {
                HistoryScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    onNavigateToEditTransaction = { transactionId ->
                        navigationState.navHostController.navigate(EditTransaction(true, transactionId))
                    },
                    modifier = modifier
                )
            },
            editAccountScreenContent = {
                EditAccountScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    modifier = modifier,
                )
            },
            addExpenseScreenContent = {
                AddTransactionScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    modifier = modifier
                )
            },
            addIncomeScreenContent = {
                AddTransactionScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    modifier = modifier
                )
            },
            editExpenseScreenContent = { isIncome, transactionId ->
                EditTransactionScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    modifier = modifier
                )
            },
            editIncomeScreenContent = { isIncome, transactionId ->
                EditTransactionScreen(
                    onNavigateBack = { navigationState.navigateBack() },
                    modifier = modifier
                )
            }
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}
