package com.example.mymoney.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.mymoney.presentation.screens.account.AccountScreen
import com.example.mymoney.presentation.screens.edit_account.EditAccountScreen

/**
 * Добавляет вложенный граф навигации для раздела "Счёт" в основной [NavGraphBuilder].
 */
fun NavGraphBuilder.accountNavGraph(
    onNavigateToEditAccount: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Account>(
        startDestination = AccountInfo
    ) {
        composable<AccountInfo> {
            AccountScreen(
                onNavigateToEditAccount = onNavigateToEditAccount,
                modifier = modifier
            )
        }

        composable<EditAccount> { backStackEntry ->
            EditAccountScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
    }
}