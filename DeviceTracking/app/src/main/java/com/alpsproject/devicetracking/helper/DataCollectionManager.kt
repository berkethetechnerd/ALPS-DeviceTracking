package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.model.SensorData

object DataCollectionManager {

    val C = ConstantsManager

    fun startWifiCollection(ctx: Activity) {
        SettingsManager.askForWiFi(ctx)
        createNewSensorEntry(AccessSensor.ACCESS_WIFI)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_WIFI, true)
    }

    fun stopWifiCollection() {
        patchSensorEntryUponFinish(AccessSensor.ACCESS_WIFI)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_WIFI, false)
    }

    fun startBluetoothCollection(ctx: Activity) {
        SettingsManager.askForBluetooth(ctx)
        createNewSensorEntry(AccessSensor.ACCESS_BLUETOOTH)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_BLUETOOTH, true)
    }

    fun stopBluetoothCollection() {
        patchSensorEntryUponFinish(AccessSensor.ACCESS_BLUETOOTH)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_BLUETOOTH, false)
    }

    fun startScreenUsageCollection() {
        createNewSensorEntry(AccessSensor.ACCESS_SCREEN_USAGE)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_SCREEN_USAGE, true)
    }

    fun stopScreenUsageCollection() {
        patchSensorEntryUponFinish(AccessSensor.ACCESS_SCREEN_USAGE)
        SharedPreferencesManager.write(C.RUNNING_SENSOR_SCREEN_USAGE, false)
    }

    private fun createNewSensorEntry(forSensor: AccessSensor) {
        val sensorName = getSensorName(forSensor)
        val sensorKey = getSensorKey(forSensor)

        val sensorData = SensorData()
        sensorData.sensorName = sensorName
        val id = RealmManager.saveData(sensorData)
        SharedPreferencesManager.write(sensorKey, id)
    }

    private fun patchSensorEntryUponFinish(forSensor: AccessSensor) {
        SharedPreferencesManager.read(getSensorKey(forSensor), "")?.let { entryId ->
            RealmManager.updateData(entryId)
        }
    }

    private fun getSensorName(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> C.SENSOR_WIFI
            AccessSensor.ACCESS_BLUETOOTH -> C.SENSOR_BLUETOOTH
            AccessSensor.ACCESS_SCREEN_USAGE -> C.SENSOR_SCREEN_USAGE
        }
    }

    private fun getSensorKey(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> C.RUNNING_SENSOR_WIFI_ID
            AccessSensor.ACCESS_BLUETOOTH -> C.RUNNING_SENSOR_BLUETOOTH_ID
            AccessSensor.ACCESS_SCREEN_USAGE -> C.RUNNING_SENSOR_SCREEN_USAGE_ID
        }
    }

}