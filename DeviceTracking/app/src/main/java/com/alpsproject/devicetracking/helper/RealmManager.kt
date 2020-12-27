package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.model.SensorData
import io.realm.Realm
import java.util.Date

object RealmManager {

    private const val ENTRY_ID = "id"
    private const val ENTRY_SENSOR_NAME = "sensorName"
    private const val ENTRY_START_DATE = "startTime"
    private const val ENTRY_END_DATE = "endTime"

    private val realm = Realm.getDefaultInstance()

    fun saveData(sensorData: SensorData): String {
        realm.beginTransaction()
        val managedSensorData = realm.copyToRealm(sensorData)
        realm.commitTransaction()

        return managedSensorData.id
    }

    fun updateData(id: String) {
        val results = realm.where(SensorData::class.java).equalTo(ENTRY_ID, id).findAll()
        if(results.size == 0) { return }

        realm.beginTransaction()

        val currentResultData = results[0]
        currentResultData?.let {
            it.endTime = Date()
            realm.copyToRealmOrUpdate(it)
        }

        realm.commitTransaction()
    }

    fun printAllData() {
        val results = realm.where(SensorData::class.java).findAll()
        results.forEach { Logger.logData(it) }
    }

    // ** QUERY : CALCULATING TOTAL HOURS FOR A SPECIFIC DATE **

    fun queryForDatesInSensor(dates: Array<String>, sensor: AccessSensor): DoubleArray {
        val data = DoubleArray(dates.size)

        for (i in data.indices) {
            data[i] = calcHoursInDate(dates[i], sensor)
        }

        return data
    }

    fun calcHoursInDate(stringDate: String, sensor: AccessSensor): Double {
        val date = CalendarManager.stringToDate(stringDate) ?: return 0.0
        val startDate = CalendarManager.extractStartDate(date)
        val endDate = CalendarManager.extractStopDate(date)
        val sensorName = ConstantsManager.getSensorName(sensor)

        var seconds = 0.0
        seconds += secondsForToday2Today(sensorName, startDate, endDate)
        seconds += secondsForBefore2Today(sensorName, startDate, endDate)
        seconds += secondsForToday2Later(sensorName, startDate, endDate)
        seconds += secondsForBefore2Later(sensorName, startDate, endDate)
        seconds += secondsForToday2NoFinish(sensorName, startDate)
        seconds += secondsForBefore2NoFinish(sensorName, startDate)
        return seconds / 3600.0
    }

    private fun secondsForToday2Today(sensor: String, startDate: Date, endDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val results = realmQuery.greaterThan(ENTRY_START_DATE, startDate).lessThan(ENTRY_END_DATE, endDate).findAll()
        return getTotalHours(results)
    }

    private fun secondsForBefore2Today(sensor: String, startDate: Date, endDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val results = realmQuery.lessThan(ENTRY_START_DATE, startDate).greaterThan(ENTRY_END_DATE, startDate).lessThan(ENTRY_END_DATE, endDate).findAll()
        return if (results.isEmpty()) { 0.0 } else { getTotalHoursWithStart(results[0]!!, startDate) }
    }

    private fun secondsForToday2Later(sensor: String, startDate: Date, endDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val results = realmQuery.greaterThan(ENTRY_START_DATE, startDate).lessThan(ENTRY_START_DATE, endDate).greaterThan(ENTRY_END_DATE, endDate).findAll()
        return if (results.isEmpty()) { 0.0 } else { getTotalHoursWithEnd(results[0]!!, endDate) }
    }

    private fun secondsForBefore2Later(sensor: String, startDate: Date, endDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val results = realmQuery.lessThan(ENTRY_START_DATE, startDate).greaterThan(ENTRY_END_DATE, endDate).findAll()
        return if (results.isEmpty()) { 0.0 } else { 24.0 * 3600 }
    }

    private fun secondsForToday2NoFinish(sensor: String, startDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val preliminaryResults = realmQuery.greaterThan(ENTRY_START_DATE, startDate).findAll()
        val results = preliminaryResults.filter { it.endTime == null }
        return getTotalHours(results)
    }

    private fun secondsForBefore2NoFinish(sensor: String, startDate: Date): Double {
        val realmQuery = realm.where(SensorData::class.java).equalTo(ENTRY_SENSOR_NAME, sensor)
        val nullDate: Date? = null
        val results = realmQuery.lessThan(ENTRY_START_DATE, startDate).equalTo(ENTRY_END_DATE, nullDate).findAll()
        return if (results.isEmpty()) { 0.0 } else { getTotalHoursWithStart(results[0]!!, startDate) }
    }

    private fun getTotalHours(results: List<SensorData>): Double {
        var seconds = 0.0

        results.forEach {
            val startTime = it.startTime
            val endTime = it.endTime ?: Date()
            seconds += (endTime.time - startTime.time) / 1000.0
        }

        return seconds
    }

    private fun getTotalHoursWithStart(result: SensorData, startTime: Date): Double {
        val endTime = result.endTime ?: Date()
        return (endTime.time - startTime.time) / 1000.0
    }

    private fun getTotalHoursWithEnd(result: SensorData, endTime: Date): Double {
        val startTime = result.startTime
        return (endTime.time - startTime.time) / 1000.0
    }
}