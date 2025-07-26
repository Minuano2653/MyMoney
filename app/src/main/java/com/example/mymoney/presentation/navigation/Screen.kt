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
object SettingsScreen

@Serializable
object AboutApp

@Serializable
object Language

@Serializable
object PrimaryColors

@Serializable
object Password

@Serializable
object Haptics

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

@Serializable
data class TransactionDetail(val isIncome: Boolean)

@Serializable
data class EditTransaction(val isIncome: Boolean, val transactionId: Int)

@Serializable
data class Analysis(val isIncome: Boolean)