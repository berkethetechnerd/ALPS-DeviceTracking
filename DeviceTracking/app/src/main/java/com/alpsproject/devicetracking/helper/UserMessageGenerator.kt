package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.app.AlertDialog
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.delegates.PermissionDelegate
import com.alpsproject.devicetracking.enums.AccessPermission
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import com.shreyaspatil.MaterialDialog.MaterialDialog

object UserMessageGenerator {

    var delegate: PermissionDelegate? = null

    fun generateDialogForAlert(activity: Activity, message: String) {
        val mDialog = AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.user_message_alert_title))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(android.R.string.ok)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()
        mDialog.show()
    }

    fun generateDialogForPermission(activity: Activity, permission: AccessPermission) {
        val sensor = getSensorTitle(activity, permission)
        val title = activity.getString(R.string.user_message_title)
        val message = activity.getString(R.string.user_message_message, sensor, sensor)

        val mBottomSheetDialog = BottomSheetMaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.user_message_positive)) { dialogInterface, _ ->
                    PermissionManager.grantPermission(permission)
                    delegate?.permissionGranted()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(activity.getString(R.string.user_message_negative)) { dialogInterface, _ ->
                    delegate?.permissionRejected()
                    dialogInterface.dismiss()
                }
                .build()
        mBottomSheetDialog.show()
    }

    private fun getSensorTitle(activity: Activity, permission: AccessPermission): String {
        return when (permission) {
            AccessPermission.ACCESS_WIFI -> activity.getString(R.string.user_message_title_wifi)
            AccessPermission.ACCESS_BLUETOOTH -> activity.getString(R.string.user_message_title_bluetooth)
            AccessPermission.ACCESS_SCREEN_USAGE -> activity.getString(R.string.user_message_title_screen_usage)
        }
    }
}