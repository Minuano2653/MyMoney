package com.example.mymoney.data.remote.dto

import com.example.mymoney.domain.entity.Category

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
}