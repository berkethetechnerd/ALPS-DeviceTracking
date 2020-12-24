package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.enums.AccessPermission
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog

object UserMessageGenerator {

    fun generateDialogForPermission(activity: Activity, permission: AccessPermission) {
        val sensor = getSensorTitle(activity, permission)
        val title = activity.getString(R.string.user_message_title)
        val message = activity.getString(R.string.user_message_message, sensor, sensor)

        val mBottomSheetDialog = BottomSheetMaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.user_message_positive)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton(activity.getString(R.string.user_message_negative)) { dialogInterface, _ ->
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