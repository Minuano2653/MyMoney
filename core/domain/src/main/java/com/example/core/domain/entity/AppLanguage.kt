package com.example.core.domain.entity

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    RUSSIAN("ru", "Русский");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return AppLanguage.entries.find { it.code == code } ?: ENGLISH
        }
    }
}