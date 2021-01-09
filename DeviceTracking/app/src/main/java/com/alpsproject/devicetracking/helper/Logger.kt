package com.alpsproject.devicetracking.helper

import android.util.Log
import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.model.SensorData

object Logger {

    private const val SERVICE_NOTIFICATION = "DTServiceNotification"
    private const val SENSOR_STATUS_CHANGE = "SensorStatusChange"
    private const val SENSOR_DATA_ENTRY = "SensorDataEntry"

    fun logServiceNotification(message: String) {
        Log.d(SERVICE_NOTIFICATION, message)
    }

    fun logSensorUpdate(sensor: AccessSensor, status: Boolean) {
        Log.d(SENSOR_STATUS_CHANGE, "$sensor -> $status")
    }

    fun logData(data: SensorData) {
        Log.d(SENSOR_DATA_ENTRY, data.toString())
    }
}