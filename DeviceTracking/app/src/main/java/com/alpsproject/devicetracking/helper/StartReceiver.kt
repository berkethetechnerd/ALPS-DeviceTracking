package com.alpsproject.devicetracking.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alpsproject.devicetracking.enums.DeviceSensor

class StartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

            val wasRunning = SharedPreferencesManager.read(ConstantsManager.SELECTED, false)

            Logger.logServiceNotification("Phone is rebooting. Starting with the data collection!")
            if (wasRunning) {
                SharedPreferencesManager.write(ConstantsManager.RUNNING_DATA_COLLECTION, true)

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_WIFI_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_WIFI), true)
                }

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_BLUETOOTH_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_BLUETOOTH), true)
                }

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_SCREEN_USAGE_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_SCREEN_USAGE), true)
                }

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_GPS_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_GPS), true)
                }

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_NFC_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_NFC), true)
                }

                if (SharedPreferencesManager.read(ConstantsManager.SENSOR_TORCH_SELECTED, false)) {
                    SharedPreferencesManager.write(ConstantsManager.getRunningSensorKey(DeviceSensor.ACCESS_TORCH), true)
                }

                context?.let { ctx ->
                    ServiceManager.startTrackerService(ctx)
                }
            }

    }
}