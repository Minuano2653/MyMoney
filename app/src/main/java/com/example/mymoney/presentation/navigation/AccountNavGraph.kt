package com.example.mymoney.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute

fun NavGraphBuilder.accountNavGraph(
    accountInfoScreenContent: @Composable () -> Unit,
    editAccountScreenContent: @Composable (Int) -> Unit,
) {
    navigation<Account>(
        startDestination = AccountInfo
    ) {
        composable<AccountInfo> {
            accountInfoScreenContent()
        }
        composable<EditAccount> { backStackEntry ->
            val args = backStackEntry.toRoute<EditAccount>()
            editAccountScreenContent(args.accountId)
        }
    }
}