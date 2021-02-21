package com.alpsproject.devicetracking.helper

import android.util.Log
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.model.SensorData

object Logger {

    private const val SERVICE_NOTIFICATION = "SMServiceNotification"
    private const val SENSOR_STATUS_CHANGE = "SensorStatusChange"
    private const val SENSOR_DATA_ENTRY = "SensorDataEntry"
    private const val SYNC_DATA_ENTRY = "DataSentToAPI"
    private const val IDENTIFIER = "SMDeviceIdentifier"

    fun logServiceNotification(message: String) {
        Log.d(SERVICE_NOTIFICATION, message)
    }

    fun logAPINotification() {
        Log.d(SYNC_DATA_ENTRY, "A data entry is sent to API")
    }

    fun logSensorUpdate(sensor: DeviceSensor, status: Boolean) {
        Log.d(SENSOR_STATUS_CHANGE, "$sensor -> $status")
    }

    fun logUniqueIdentifier(id: String, isNew: Boolean) {
        if (isNew) {
            Log.d(IDENTIFIER, "A new device id has been assigned: $id")
        } else {
            Log.d(IDENTIFIER, "Your device id was assigned as $id")
        }
    }

    fun logData(data: SensorData) {
        Log.d(SENSOR_DATA_ENTRY, data.toString())
    }
}