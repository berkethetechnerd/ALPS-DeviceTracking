package com.alpsproject.devicetracking.helper

enum class AccessPermission {
    ACCESS_WIFI,
    ACCESS_BLUETOOTH,
    ACCESS_SCREEN_USAGE
}

object PermissionManager {

    fun checkPermission(forPermission: AccessPermission): Boolean {
        return when (forPermission) {
            AccessPermission.ACCESS_WIFI -> {
                SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_WIFI, false)
            }
            AccessPermission.ACCESS_BLUETOOTH -> {
                SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_BLUETOOTH, false)
            }
            AccessPermission.ACCESS_SCREEN_USAGE -> {
                SharedPreferencesManager.read(ConstantsManager.CONSENT_OF_SCREEN_USAGE, false)
            }
        }

    }

    fun askPermission(forPermission: AccessPermission) {
        when (forPermission) {
            AccessPermission.ACCESS_WIFI -> {
                //UserMessageGenerator.generateDialogForPermission(AccessPermission.ACCESS_WIFI)
            }
            AccessPermission.ACCESS_BLUETOOTH -> {
                //UserMessageGenerator.generateDialogForPermission(AccessPermission.ACCESS_BLUETOOTH)
            }
            AccessPermission.ACCESS_SCREEN_USAGE -> {
                //UserMessageGenerator.generateDialogForPermission(AccessPermission.ACCESS_SCREEN_USAGE)
            }
        }
    }
}