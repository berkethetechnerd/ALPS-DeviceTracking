package com.alpsproject.devicetracking.helper

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object CalendarManager {

    private const val DAY_FORMAT = "dd.MM"

    fun last7Days(): Array<String> {
        val today = Calendar.getInstance()
        today.add(Calendar.DATE, -6)

        val arrayOfDays = ArrayList<String>()
        arrayOfDays.add(calendarToString(today))

        for (i in 2..7) {
            today.add(Calendar.DATE, 1)
            arrayOfDays.add(calendarToString(today))
        }

        return arrayOfDays.toTypedArray()
    }

    private fun calendarToString(date: Calendar): String {
        val formatter = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
        return formatter.format(date.time)
    }
}