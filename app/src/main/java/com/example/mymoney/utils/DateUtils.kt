package com.example.mymoney.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
        Log.d("DateUtils", "calendar.time = $date")
        Log.d("DateUtils", "formatted = ${formatter.format(date)}")
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

    fun formatDate(date: Date): String {
        return formatter.format(date)
    }
}