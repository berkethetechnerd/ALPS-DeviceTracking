package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.CalendarDays
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

object CalendarManager {

    private const val DAY_FORMAT = "dd.MM.yyyy"
    private const val BACKEND_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

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

    fun extractStartDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 500)

        return calendar.time
    }

    fun extractStopDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 500)

        return calendar.time
    }

    fun stringToDate(dateString: String): Date? = getFormatter().parse(dateString)

    private fun calendarToString(date: Calendar): String = getFormatter().format(date.time)

    fun convertDateForBackend(date: Date?): String? {
        date?.let {
            return getBackendFormatter().format(date.time)
        }

        return null
    }


    private fun getFormatter(): SimpleDateFormat = SimpleDateFormat(DAY_FORMAT, Locale.getDefault())

    private fun getBackendFormatter(): SimpleDateFormat = SimpleDateFormat(BACKEND_DATE_FORMAT, Locale.getDefault())

    private fun getLowerCalendarLimit(type: CalendarDays): Int {
        return when(type) {
            CalendarDays.LAST_24_HOURS -> 1
            CalendarDays.LAST_3_DAYS -> 3
            CalendarDays.LAST_7_DAYS -> 7
            CalendarDays.LAST_15_DAYS -> 15
        }
    }
}