package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.enums.AccessSensor

object PermissionManager {

    fun checkPermission(forSensor: AccessSensor): Boolean {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_WIFI, false)
            AccessSensor.ACCESS_BLUETOOTH -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_BLUETOOTH, false)
            AccessSensor.ACCESS_SCREEN_USAGE -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_SCREEN_USAGE, false)
        }
    }

    fun askPermission(context: Activity, forSensor: AccessSensor) {
        when (forSensor) {
            AccessSensor.ACCESS_WIFI -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_WIFI)
            AccessSensor.ACCESS_BLUETOOTH -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_BLUETOOTH)
            AccessSensor.ACCESS_SCREEN_USAGE -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_SCREEN_USAGE)
        }
    }

    fun grantPermission(forSensor: AccessSensor) {
        val key = when (forSensor) {
            AccessSensor.ACCESS_WIFI -> ConstantsManager.CONSENT_OF_WIFI
            AccessSensor.ACCESS_BLUETOOTH -> ConstantsManager.CONSENT_OF_BLUETOOTH
            AccessSensor.ACCESS_SCREEN_USAGE -> ConstantsManager.CONSENT_OF_SCREEN_USAGE
        }

        SharedPreferencesManager.write(key, true)
    }
}