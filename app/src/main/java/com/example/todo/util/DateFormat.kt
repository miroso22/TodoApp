package com.example.todo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "dd.MM.yyyy"

object DateFormatter {
    private val formatter get() = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    fun millisToString(millis: Long): String = formatter.format(Date(millis))

    fun stringToMillis(string: String) = formatter.parse(string)?.time

    fun startDayMillis(millis: Long) = stringToMillis(millisToString(millis)) ?: 0L
}
