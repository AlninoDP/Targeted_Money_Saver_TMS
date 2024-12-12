package com.tms.targetedmoneysaver.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Utility function to add days to a date
fun addDaysToDate(dateString: String, daysToAdd: Long): String {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val inputDate = LocalDate.parse(dateString, formatter)
    val newDate = inputDate.plusDays(daysToAdd)

    return newDate.format(formatter)
}
