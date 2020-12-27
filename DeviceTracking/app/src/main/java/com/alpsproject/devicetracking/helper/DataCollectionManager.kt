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
        val sensorName = C.getSensorName(forSensor)
        val sensorKey = C.getRunningSensorKey(forSensor)

        val sensorData = SensorData()
        sensorData.sensorName = sensorName
        val id = RealmManager.saveData(sensorData)
        SharedPreferencesManager.write(sensorKey, id)
    }

    private fun patchSensorEntryUponFinish(forSensor: AccessSensor) {
        SharedPreferencesManager.read(C.getRunningSensorKey(forSensor), "")?.let { entryId ->
            RealmManager.updateData(entryId)
        }
    }

}