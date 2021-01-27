package com.alpsproject.devicetracking.helper

import android.util.Log
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.model.SensorData

object Logger {

    private const val SERVICE_NOTIFICATION = "DTServiceNotification"
    private const val SENSOR_STATUS_CHANGE = "SensorStatusChange"
    private const val SENSOR_DATA_ENTRY = "SensorDataEntry"
    private const val SYNC_DATA_ENTRY = "DataSentToAPI"

    fun logServiceNotification(message: String) {
        Log.d(SERVICE_NOTIFICATION, message)
    }

    fun logAPINotification() {
        Log.d(SYNC_DATA_ENTRY, "A data entry is sent to API")
    }

    fun logSensorUpdate(sensor: DeviceSensor, status: Boolean) {
        Log.d(SENSOR_STATUS_CHANGE, "$sensor -> $status")
    }

    fun logData(data: SensorData) {
        Log.d(SENSOR_DATA_ENTRY, data.toString())
    }
}