package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.helper.ConstantsManager as C

object PermissionManager {

    fun checkPermission(sensor: DeviceSensor): Boolean {
        val consentKey = C.getConsentSensorKey(sensor)
        return SharedPreferencesManager.read(consentKey, false)
    }

    fun askPermission(activity: Activity, sensor: DeviceSensor) {
        UserMessageGenerator.generateDialogForPermission(activity, sensor)
    }

    fun grantPermission(forSensor: DeviceSensor) {
        val consentKey = C.getConsentSensorKey(forSensor)
        SharedPreferencesManager.write(consentKey, true)
    }
}