package com.example.mymoney.data.remote.dto

data class UpdateAccountRequest(
    val name: String,
    val balance: String,
    val currency: String
)