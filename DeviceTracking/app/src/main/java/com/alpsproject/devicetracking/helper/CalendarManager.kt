package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.CalendarDays
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object CalendarManager {

    private const val DAY_FORMAT = "dd.MM"

    fun fetchCalendarDays(type: CalendarDays): Array<String> {
        val arrayOfDays = ArrayList<String>()
        val lowerLimit = getLowerCalendarLimit(type)
        val today = Calendar.getInstance()

        today.add(Calendar.DATE, -(lowerLimit - 1))
        arrayOfDays.add(calendarToString(today))

        for (dayNo in 1 until lowerLimit) {
            today.add(Calendar.DATE, 1)
            arrayOfDays.add(calendarToString(today))
        }

        return arrayOfDays.toTypedArray()
    }

    private fun getLowerCalendarLimit(type: CalendarDays): Int {
        return when(type) {
            CalendarDays.LAST_24_HOURS -> 1
            CalendarDays.LAST_3_DAYS -> 3
            CalendarDays.LAST_7_DAYS -> 7
            CalendarDays.LAST_15_DAYS -> 15
        }
    }

    private fun calendarToString(date: Calendar): String {
        val formatter = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())
        return formatter.format(date.time)
    }
}