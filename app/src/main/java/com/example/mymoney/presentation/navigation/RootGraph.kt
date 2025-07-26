package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymoney.presentation.screens.main.MainScreen
import com.example.mymoney.presentation.screens.settings.pincode.enter.EnterPinScreen
import com.example.mymoney.presentation.screens.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object EnterPin

@Serializable
object Main

@Composable
fun RootGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Splash
    ) {
        composable<Splash> {
            SplashScreen(navController)
        }
        composable<Main> {
            MainScreen()
        }
        composable<EnterPin>{
            EnterPinScreen(
                onPinVerified = {
                    navController.navigate(Main) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            )
        }
    }
}
