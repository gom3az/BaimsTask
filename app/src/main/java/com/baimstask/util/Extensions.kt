package com.baimstask.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.formattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(this, formatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy  hh a")
    return dateTime.format(outputFormatter)
}