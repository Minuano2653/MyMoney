package com.example.mymoney.data.remote.dto

/**
 * DTO-модель элемента, связанного со статьёй.
 *
 * @property categoryId Идентификатор статьи.
 * @property categoryName Название категории.
 * @property emoji Эмодзи, связанный с категорией.
 * @property amount Сумма в виде строки.
 */
data class StatItemDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: String
)
