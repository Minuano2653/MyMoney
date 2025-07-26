package com.example.mymoney.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.entity.Category

@Entity(
    tableName = "category"
)
data class LocalCategory(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) {
    fun toDomain() = Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )
}