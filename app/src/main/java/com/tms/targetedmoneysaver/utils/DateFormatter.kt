package com.tms.targetedmoneysaver.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Utility function to add days to a date
fun addDaysToDate(dateString: String, daysToAdd: Long): String {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val inputDate = LocalDate.parse(dateString, formatter)
    val newDate = inputDate.plusDays(daysToAdd)

    return newDate.format(formatter)
}

// Utility function to format a date string (d/M/yyyy format)
fun formatDateFromString(dateString: String): String {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val inputDate = LocalDate.parse(dateString, formatter)

    return inputDate.format(formatter)
}