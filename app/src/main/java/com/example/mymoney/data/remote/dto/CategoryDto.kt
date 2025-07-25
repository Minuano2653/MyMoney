package com.example.mymoney.data.remote.dto

import com.example.core.domain.entity.Category
import com.example.mymoney.data.local.entity.LocalCategory

/**
 * DTO-модель статьи, получаемая с сервера.
 *
 * @property id Идентификатор статьи.
 * @property name Название статьи.
 * @property emoji Эмодзи, связанный с статьёй.
 * @property isIncome Флаг, обозначающий, относится ли статься к доходам.
 */
data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) {
    fun toDomain(): Category {
        return Category(
            id = id,
            name = name,
            emoji = emoji,
            isIncome = isIncome
        )
    }

    fun toLocal(): LocalCategory {
        return LocalCategory(
            id = id,
            name = name,
            emoji = emoji,
            isIncome = isIncome
        )
    }
}
