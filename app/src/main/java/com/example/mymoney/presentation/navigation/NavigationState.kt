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

    fun navigateTo(destination: Any) {
        navHostController.navigate(destination) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToEditAccount(accountId: Int) {
        navHostController.navigate(EditAccount(accountId = accountId))
    }

    fun navigateBack() {
        navHostController.popBackStack()
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
