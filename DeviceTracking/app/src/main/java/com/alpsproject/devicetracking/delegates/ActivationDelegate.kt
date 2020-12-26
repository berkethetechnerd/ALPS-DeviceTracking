package com.alpsproject.devicetracking.delegates

import android.app.Activity
import com.alpsproject.devicetracking.enums.AccessSensor

interface ActivationDelegate {
    fun sensorActivated(context: Activity, sensor: AccessSensor)
}