package com.example.core.common.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    val yearMonthDayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val dayMonthYearFormatter = SimpleDateFormat("dd.MM.yyyy", Locale("ru", "RU")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val isoFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale("ru", "RU")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    val dayMonthFormatter = SimpleDateFormat("dd.MM", Locale("ru", "RU"))

    val dayMonthYearTimeFormatter =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    val timeFormatter = SimpleDateFormat("HH:mm", Locale("ru", "RU")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getTodayYearMonthDayFormatted(): String {
        return yearMonthDayFormatter.format(Date())
    }

    fun getFirstDayOfCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return yearMonthDayFormatter.format(calendar.time)
    }

    fun formatDateFromMillis(millis: Long): String {
        return yearMonthDayFormatter.format(Date(millis))
    }

    fun toMillis(dateString: String): Long? {
        return try {
            yearMonthDayFormatter.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun isoToMillis(isoString: String): Long? {
        return try {
            isoFormatter.parse(isoString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun formatIsoToDayMonth(isoString: String): String? {
        return try {
            val date = isoFormatter.parse(isoString)
            date?.let { dayMonthFormatter.format(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun formatToDayMonthYear(millis: Long): String {
        return dayMonthYearFormatter.format(Date(millis))
    }

    fun dayMonthYearToMillis(dateString: String): Long? {
        return try {
            dayMonthYearFormatter.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    fun getTodayDayMonthYearFormatted(): String {
        return dayMonthYearFormatter.format(Date())
    }

    fun toIsoToday(yearMonthDayString: String): String {
        val date = yearMonthDayFormatter.parse(yearMonthDayString)
        return isoFormatter.format(date!!)
    }


    fun combineDateAndTimeToIso(date: String, time: String): String? {
        return try {
            val combined = dayMonthYearTimeFormatter.parse("$date $time")
            combined?.let { isoFormatter.format(it) }
        } catch (e: Exception) {
            null
        }
    }

    fun splitIsoToDateAndTime(isoString: String): Pair<String, String>? {
        return try {
            val date = isoFormatter.parse(isoString)
            if (date != null) {
                val datePart = dayMonthYearFormatter.format(date)
                val timePart = timeFormatter.format(date)
                datePart to timePart
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun parseTimeToHourMinute(time: String): Pair<Int, Int> {
        val parts = time.split(":").mapNotNull { it.toIntOrNull() }
        val hour = parts.getOrNull(0)?.coerceIn(0, 23) ?: 0
        val minute = parts.getOrNull(1)?.coerceIn(0, 59) ?: 0
        return hour to minute
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format(Locale("ru", "RU") ,"%02d:%02d", hour, minute)
    }

    fun formatDateWithMonthInGenitive(dateString: String): String {
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))

        val date = yearMonthDayFormatter.parse(dateString) ?: return ""
        return outputFormat.format(date)
    }

    fun getCurrentIso(): String {
        val currentTime = Calendar.getInstance().time
        return isoFormatter.format(currentTime)
    }

    fun getStartAndEndDateRange(days: Int): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val endDate = formatDateFromMillis(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_MONTH, -days)
        val startDate = formatDateFromMillis(calendar.timeInMillis)
        return startDate to endDate
    }

    fun getDateListFromToday(days: Int): List<String> {
        val calendar = Calendar.getInstance()
        val list = mutableListOf<String>()
        for (i in 0 until days) {
            val dateStr = dayMonthFormatter.format(calendar.time)
            list.add(dateStr)
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }
        return list.reversed()
    }
}