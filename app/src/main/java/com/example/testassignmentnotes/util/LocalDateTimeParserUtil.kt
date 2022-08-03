package com.example.testassignmentnotes.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.parseToString(): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm")
    return formatter.format(this)
}