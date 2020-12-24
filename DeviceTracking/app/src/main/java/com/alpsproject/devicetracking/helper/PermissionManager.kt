package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.enums.AccessPermission

object PermissionManager {

    fun checkPermission(forPermission: AccessPermission): Boolean {
        return when (forPermission) {
            AccessPermission.ACCESS_WIFI -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_WIFI, false)
            AccessPermission.ACCESS_BLUETOOTH -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_BLUETOOTH, false)
            AccessPermission.ACCESS_SCREEN_USAGE -> SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_SCREEN_USAGE, false)
        }
    }

    fun askPermission(context: Activity, forPermission: AccessPermission) {
        when (forPermission) {
            AccessPermission.ACCESS_WIFI -> UserMessageGenerator.generateDialogForPermission(context, AccessPermission.ACCESS_WIFI)
            AccessPermission.ACCESS_BLUETOOTH -> UserMessageGenerator.generateDialogForPermission(context, AccessPermission.ACCESS_BLUETOOTH)
            AccessPermission.ACCESS_SCREEN_USAGE -> UserMessageGenerator.generateDialogForPermission(context, AccessPermission.ACCESS_SCREEN_USAGE)
        }
    }

    fun grantPermission(forPermission: AccessPermission) {
        val key = when (forPermission) {
            AccessPermission.ACCESS_WIFI -> ConstantsManager.CONSENT_OF_WIFI
            AccessPermission.ACCESS_BLUETOOTH -> ConstantsManager.CONSENT_OF_BLUETOOTH
            AccessPermission.ACCESS_SCREEN_USAGE -> ConstantsManager.CONSENT_OF_SCREEN_USAGE
        }

        SharedPreferencesManager.write(key, true)
    }
}