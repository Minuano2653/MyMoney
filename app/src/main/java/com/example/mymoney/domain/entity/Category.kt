package com.example.mymoney.domain.entity


/**
 * Доменная модель статьи.
 *
 * @property id Уникальный идентификатор статьи.
 * @property name Название статьи.
 * @property emoji Эмодзи, ассоциированное с статьёй.
 * @property isIncome Флаг, указывающий, является ли статья доходной.
 */
data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
