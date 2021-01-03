package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.enums.AccessSensor

object PermissionManager {

    fun checkPermission(forSensor: AccessSensor): Boolean {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_WIFI, false)
            AccessSensor.ACCESS_BLUETOOTH -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_BLUETOOTH, false)
            AccessSensor.ACCESS_SCREEN_USAGE -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_SCREEN_USAGE, false)
            AccessSensor.ACCESS_MOBILE_DATA -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_MOBILE_DATA, false)
        }
    }

    fun askPermission(context: Activity, forSensor: AccessSensor) {
        when (forSensor) {
            AccessSensor.ACCESS_WIFI -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_WIFI)
            AccessSensor.ACCESS_BLUETOOTH -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_BLUETOOTH)
            AccessSensor.ACCESS_SCREEN_USAGE -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_SCREEN_USAGE)
            AccessSensor.ACCESS_MOBILE_DATA -> UserMessageGenerator.generateDialogForPermission(context, AccessSensor.ACCESS_MOBILE_DATA)
        }
    }

    fun grantPermission(forSensor: AccessSensor) {
        val consentKey = ConstantsManager.getConsentSensorKey(forSensor)
        SharedPreferencesManager.write(consentKey, true)
    }
}