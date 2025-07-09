package com.example.mymoney.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Expenses

@Serializable
object Incomes

@Serializable
object Account

@Serializable
object Categories

@Serializable
object Settings

@Serializable
object ExpensesToday

@Serializable
object IncomesToday

@Serializable
data class TransactionsHistory(val isIncome: Boolean)

@Serializable
object AccountInfo

@Serializable
data class EditAccount(val accountId: Int)

