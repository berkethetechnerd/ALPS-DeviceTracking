package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.app.AlertDialog
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.delegates.ActivationDelegate
import com.alpsproject.devicetracking.delegates.PermissionDelegate
import com.alpsproject.devicetracking.enums.AccessSensor
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog

object UserMessageGenerator {

    var activationDelegate: ActivationDelegate? = null
    var permissionDelegate: PermissionDelegate? = null

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

    fun generateDialogForPermission(activity: Activity, sensor: AccessSensor) {
        val typeOfSensor = ConstantsManager.getSensorName(sensor)
        val title = activity.getString(R.string.user_message_title)
        val message = activity.getString(R.string.user_message_message, typeOfSensor, typeOfSensor)

        val mBottomSheetDialog = BottomSheetMaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.user_message_positive)) { dialogInterface, _ ->
                    PermissionManager.grantPermission(sensor)
                    permissionDelegate?.permissionGranted()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(activity.getString(R.string.user_message_negative)) { dialogInterface, _ ->
                    permissionDelegate?.permissionRejected()
                    dialogInterface.dismiss()
                }
                .build()
        mBottomSheetDialog.show()
    }

    fun generateDialogForActivation(activity: Activity, sensor: AccessSensor) {
        val typeOfSensor = ConstantsManager.getSensorName(sensor)
        val title = activity.getString(R.string.user_message_sensor_activation_title)
        val message = activity.getString(R.string.user_message_sensor_activation_message, typeOfSensor)

        val mBottomSheetDialog = BottomSheetMaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.user_message_sensor_activation_confirm)) { dialogInterface, _ ->
                    activationDelegate?.sensorActivated(activity, sensor)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(activity.getString(R.string.user_message_sensor_activation_reject)) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .build()
        mBottomSheetDialog.show()
    }
}