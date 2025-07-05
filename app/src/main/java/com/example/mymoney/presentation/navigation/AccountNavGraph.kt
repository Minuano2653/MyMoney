package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation

fun NavGraphBuilder.accountNavGraph(
    accountInfoScreenContent: @Composable () -> Unit,
    editAccountScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.AccountInfo.route,
        route = Screen.Account.route
    ) {
        composable(
            route = Screen.AccountInfo.route,
        ) {
            accountInfoScreenContent()
        }
        composable(
            route = "${Screen.EditAccount.route}/{${Screen.ARGUMENT_ACCOUNT_ID}}",
            arguments = listOf(
                navArgument(Screen.ARGUMENT_ACCOUNT_ID) { type = NavType.IntType }
            )
        ) {
            editAccountScreenContent()
        }
    }
}