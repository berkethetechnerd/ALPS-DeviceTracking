package com.alpsproject.devicetracking.delegates

import android.app.Activity
import com.alpsproject.devicetracking.enums.DeviceSensor

interface ActivationDelegate {
    fun sensorActivationRequested(activity: Activity, sensor: DeviceSensor)
}