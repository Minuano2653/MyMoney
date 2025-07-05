package com.example.mymoney.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Утилитный объект для работы с датами в формате "yyyy-MM-dd" и ISO-строками.
 *
 * Использует часовой пояс UTC и локаль "ru_RU".
 *
 * Основные функции:
 * - Получение сегодняшней даты в формате "yyyy-MM-dd".
 * - Получение первой даты текущего месяца в формате "yyyy-MM-dd".
 * - Форматирование даты из миллисекунд в строку "yyyy-MM-dd".
 * - Парсинг даты из строки в миллисекунды.
 * - Преобразование ISO-формата даты ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") в миллисекунды.
 * - Форматирование ISO-строки в формат "dd.MM".
 */
object DateUtils {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("ru", "RU")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val outputFormatter = SimpleDateFormat("dd.MM", Locale("ru", "RU"))

    fun getTodayFormatted(): String {
        return formatter.format(Date())
    }

    fun getFirstDayOfCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val date = calendar.time
        return formatter.format(calendar.time)
    }

    fun formatDateFromMillis(millis: Long): String {
        return formatter.format(Date(millis))
    }

    fun toMillis(dateString: String): Long? {
        return try {
            formatter.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun isoToMillis(isoString: String): Long? {
        return try {
            inputFormatter.parse(isoString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun formatIsoToDayMonth(isoString: String): String? {
        return try {
            val date = inputFormatter.parse(isoString)
            date?.let { outputFormatter.format(it) }
        } catch (e: Exception) {
            null
        }
    }
}