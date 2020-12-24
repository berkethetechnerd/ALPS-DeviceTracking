package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

enum class AccessPermission {
    ACCESS_WIFI,
    ACCESS_BLUETOOTH,
    ACCESS_SCREEN_USAGE
}

object PermissionManager: ActivityCompat.OnRequestPermissionsResultCallback {

    private const val REQUEST_WIFI = android.Manifest.permission.ACCESS_WIFI_STATE
    private const val REQUEST_WIFI_CODE = 101

    private var activeActivity: Activity? = null

    fun checkPermission(ctx: Context, forPermission: AccessPermission): Boolean {
        if (forPermission == AccessPermission.ACCESS_WIFI) {
            return ContextCompat.checkSelfPermission(ctx, REQUEST_WIFI) == PackageManager.PERMISSION_GRANTED
        }

        return false
    }

    fun askPermission(activity: Activity, forPermission: AccessPermission) {
        this.activeActivity = activity

        if (forPermission == AccessPermission.ACCESS_WIFI) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(REQUEST_WIFI),
                    REQUEST_WIFI_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        activeActivity?.let { caller ->
            if (grantResults.isEmpty()) {
                return // No permission given
            }

            if (requestCode == REQUEST_WIFI_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SettingsManager.turnWifiOn(caller)
                } else {
                    // TODO: Error handling
                }
            }
        }
    }
}