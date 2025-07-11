package com.example.mymoney.data.remote.dto

data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)