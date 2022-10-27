package com.core.utils

import java.text.SimpleDateFormat
import java.util.*

//2021-08-30 02:22:56
const val DEFAULT_FROM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val HOURS_FORMAT = "HH:mm"

const val DEFAULT_TO_DATE_FORMAT_FLOAT_SECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

//2021-11-25T23:00:00Z
//2020-12-30T14:25:56.371432Z
//2021-02-04T20:03:14.145203Z
const val DEFAULT_TO_DATE_FORMAT_WITH_MILLIS = "HH:mm"
const val DEFAULT_TO_DATE_FORMAT = "dd.MM.yyyy HH:mm"
const val FILTER_TO_DATE_FORMAT = "dd.MM.yyyy"

val TIMEZONE_UTC: TimeZone = TimeZone.getTimeZone("UTC")

fun Date.convertToString(format: String = DEFAULT_FROM_DATE_FORMAT, timeZone: TimeZone = TIMEZONE_UTC): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}


fun String.convertToLocal(pattern: String = DEFAULT_TO_DATE_FORMAT): String {
    val format1 = SimpleDateFormat(DEFAULT_FROM_DATE_FORMAT)
    val format2 = SimpleDateFormat(pattern)
    val date: Date = format1.parse(this)
    return format2.format(date)
}

object DateConverter {

    private val defaultFormatter = SimpleDateFormat(DEFAULT_FROM_DATE_FORMAT, Locale.getDefault())
            .apply { timeZone = TIMEZONE_UTC }

    fun toDate(stringDate: String): Date? {
        return runCatching { defaultFormatter.parse(stringDate) }.getOrNull()
    }

    fun toDate(stringDate: String, format: String, isTimeZoneUTC: Boolean = true): Date? {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        if (isTimeZoneUTC)
            formatter.timeZone = TIMEZONE_UTC
        return runCatching { formatter.parse(stringDate) }.getOrNull()
    }
}