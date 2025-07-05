package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Класс-обёртка над [NavHostController] для управления навигацией с сохранением состояния.
 *
 * @param navHostController Контроллер навигации Jetpack Compose.
 */
class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    fun navigateToHistory(route: String, isIncome: Boolean) {
        when(isIncome) {
            true -> {
                navHostController.navigate("$route/true")
            }
            false -> {
                navHostController.navigate("$route/false")
            }
        }
    }
    fun navigateToEditAccount(route: String, accountId: Int) {
        navHostController.navigate("$route/$accountId")
    }
}

/**
 * Хелпер-функция для запоминания [NavigationState] внутри Composable.
 *
 * @param navHostController Контроллер навигации (по умолчанию создаётся новый).
 * @return remember [NavigationState].
 */
@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}
