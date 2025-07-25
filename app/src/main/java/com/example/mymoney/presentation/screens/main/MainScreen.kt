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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.presentation.daggerViewModel
import com.example.mymoney.R
import com.example.mymoney.presentation.navigation.Account
import com.example.mymoney.presentation.navigation.AppNavGraph
import com.example.mymoney.presentation.navigation.BottomNavItem
import com.example.mymoney.presentation.navigation.Categories
import com.example.mymoney.presentation.navigation.Expenses
import com.example.mymoney.presentation.navigation.Incomes
import com.example.mymoney.presentation.navigation.Settings
import com.example.mymoney.presentation.theme.MyMoneyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navHostController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = daggerViewModel()
) {
    val context = LocalContext.current
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val navigationList = listOf(
                BottomNavItem(Expenses, R.string.bottom_label_expenses, R.drawable.ic_expenses),
                BottomNavItem(Incomes, R.string.bottom_label_incomes, R.drawable.ic_incomes),
                BottomNavItem(Account, R.string.bottom_label_account, R.drawable.ic_account),
                BottomNavItem(
                    Categories,
                    R.string.bottom_label_categories,
                    R.drawable.ic_categories
                ),
                BottomNavItem(Settings, R.string.bottom_label_settings, R.drawable.ic_settings)
            )

            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                navigationList.forEach { item ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navHostController.navigate(item.route) {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
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
            navHostController = navHostController
        )
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is MainSideEffect.ShowNetworkStatus -> {
                    snackbarHostState.showSnackbar(context.getString(effect.messageRes))
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MyMoneyTheme {
        MainScreen()
    }
}
