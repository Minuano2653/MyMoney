package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.presentation.screens.MainScreen
import com.example.mymoney.presentation.screens.splash.SplashScreen


/**
 * Корневой граф навигации приложения.
 *
 * Содержит начальный экран загрузки ("splash") и основной экран приложения ("main").
 *
 * @param navController Контроллер навигации (по умолчанию создаётся с помощью [rememberNavController]).
 */
@Composable
fun RootGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("main") {
            MainScreen()
        }
    }
}
