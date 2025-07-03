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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymoney.presentation.navigation.AppNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.navigation.Screen
import com.example.mymoney.presentation.navigation.rememberNavigationState
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.history.HistoryScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val navigationState = rememberNavigationState()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

            val navigationList = listOf(
                BottomNavItem.Expenses,
                BottomNavItem.Incomes,
                BottomNavItem.Account,
                BottomNavItem.Categories,
                BottomNavItem.Settings
            )

            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                navigationList.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navigationState.navigateTo(item.screen.route)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
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
                        navigationState.navigateToHistory(
                            route = Screen.ROUTE_EXPENSES_HISTORY,
                            isIncome = false
                        )
                    },
                    onNavigateToAddExpense = { /*navigationState.navigateTo("навигация на экран добавления")*/ },
                    onNavigateToTransactionDetail = { /*navigationState.navigateTo("навигация на детальную инфу")*/ },
                    modifier = modifier
                )
            },
            incomesTodayScreenContent = {
                IncomesScreen(
                    onNavigateToHistory = {
                        navigationState.navigateToHistory(
                            route = Screen.ROUTE_INCOMES_HISTORY,
                            isIncome = true
                        )
                    },
                    onNavigateToAddIncome = { /*navigationState.navigateTo("навигация на экран добавления")*/ },
                    onNavigateToTransactionDetail = { /*navigationState.navigateTo("навигация на экран добавления")*/ },
                    modifier = modifier
                )
            },
            accountScreenContent = {
                AccountScreen(
                    onNavigateToEditAccount = { /*navigationState.navigateTo("Навигация на экран редактирования")*/ },
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
                    onNavigateBack = { navigationState.navHostController.popBackStack() },
                    modifier = modifier
                )
            },
            incomesHistoryScreenContent = {
                HistoryScreen(
                    onNavigateBack = { navigationState.navHostController.popBackStack() },
                    modifier = modifier
                )
            }
        )
    }
}
