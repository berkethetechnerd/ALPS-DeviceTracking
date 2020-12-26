package com.alpsproject.devicetracking.helper

import android.util.Log
import com.alpsproject.devicetracking.enums.AccessSensor

object Logger {

    fun logSensorUpdate(sensor: AccessSensor, status: Boolean) {
        Log.d("SensorStatusChange", "$sensor -> $status")
    }
}