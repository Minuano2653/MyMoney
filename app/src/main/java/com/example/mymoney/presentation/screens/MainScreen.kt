package com.example.mymoney.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.navigation.AppNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.navigation.FabState
import com.example.mymoney.presentation.navigation.TopAppBarState
import com.example.mymoney.presentation.navigation.rememberNavigationState
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.transactions_history.ExpensesHistoryScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.screens.transactions_history.IncomesHistoryScreen
import com.example.mymoney.ui.theme.MyMoneyTheme

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    var topAppBarState by remember {
        mutableStateOf(TopAppBarState())
    }
    var fabState by remember {
        mutableStateOf(FabState())
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CustomTopAppBar(topAppBarState)
        },

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

        floatingActionButton = {
            if (fabState.isVisible && fabState.onClick != null) {
                FloatingActionButton(
                    onClick = fabState.onClick!!,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(fabState.iconRes),
                        contentDescription = "FAB"
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            modifier = Modifier.padding(paddingValues),
            expensesTodayScreenContent = {
                ExpensesScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState
                )
            },
            incomesTodayScreenContent = {
                IncomesScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState
                )
            },
            accountScreenContent = {
                AccountScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState
                )
            },
            categoriesScreenContent = {
                CategoriesScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState
                )
            },
            settingsScreenContent = {
                SettingsScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState }
                )
            },
            expensesHistoryScreenContent = {
                ExpensesHistoryScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState

                )
            },
            incomesHistoryScreenContent = {
                IncomesHistoryScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    navHostController = navigationState.navHostController,
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }
}