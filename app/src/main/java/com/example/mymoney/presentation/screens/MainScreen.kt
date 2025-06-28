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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymoney.R
import com.example.mymoney.presentation.components.CustomTopAppBar
import com.example.mymoney.presentation.navigation.AppNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.components.model.FabState
import com.example.mymoney.presentation.components.model.TopAppBarState
import com.example.mymoney.presentation.navigation.rememberNavigationState
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.categories.CategoriesScreen
import com.example.mymoney.presentation.screens.expenses.ExpensesScreen
import com.example.mymoney.presentation.screens.history.expenses.ExpensesHistoryScreen
import com.example.mymoney.presentation.screens.incomes.IncomesScreen
import com.example.mymoney.presentation.screens.settings.SettingsScreen
import com.example.mymoney.presentation.screens.history.incomes.IncomesHistoryScreen
import com.example.mymoney.presentation.theme.MyMoneyTheme

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}

/**
 * Главный экран приложения с навигацией и базовым UI-скелетом.
 *
 * Содержит Scaffold с верхней панелью (TopAppBar), нижней навигационной панелью (NavigationBar),
 * плавающей кнопкой действия (FAB) и Snackbar для отображения сообщений.
 *
 * Управляет состоянием TopAppBar, FAB и навигацией между основными разделами приложения:
 * Расходы, Доходы, Счёт, Категории и Настройки.
 *
 * Использует [AppNavGraph] для построения навигационного графа с экранами:
 * - ExpensesScreen
 * - IncomesScreen
 * - AccountScreen
 * - CategoriesScreen
 * - SettingsScreen
 * - ExpensesHistoryScreen
 * - IncomesHistoryScreen
 *
 * Взаимодействие с UI-элементами происходит через лямбды для обновления состояний TopAppBar и FAB,
 * а также для навигации и показа snackbar.
 */
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
                        contentDescription = stringResource(R.string.fab_description)
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
                    onNavigateToHistory = { route -> navigationState.navigateTo(route) },
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
                )
            },
            incomesTodayScreenContent = {
                IncomesScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    onNavigateToHistory = { route -> navigationState.navigateTo(route) },
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
                )
            },
            accountScreenContent = {
                AccountScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
                )
            },
            categoriesScreenContent = {
                CategoriesScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
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
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
                    onNavigateBack = { navigationState.navHostController.popBackStack() },
                )
            },
            incomesHistoryScreenContent = {
                IncomesHistoryScreen(
                    onUpdateTopAppBar = { newState -> topAppBarState = newState },
                    onUpdateFabState = { newState -> fabState = newState },
                    onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
                    onNavigateBack = { navigationState.navHostController.popBackStack() },
                )
            }
        )
    }
}
