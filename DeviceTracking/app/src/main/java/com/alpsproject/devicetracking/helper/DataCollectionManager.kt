package com.alpsproject.devicetracking.helper

import android.app.Activity
import com.alpsproject.devicetracking.api.API
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.model.SensorData
import com.alpsproject.devicetracking.helper.ConstantsManager as C

object DataCollectionManager {

    fun startCollectionForSensor(sensor: DeviceSensor, activity: Activity) {
        when (sensor) {
            DeviceSensor.ACCESS_WIFI -> startWifiCollection(activity)
            DeviceSensor.ACCESS_BLUETOOTH -> startBluetoothCollection(activity)
            DeviceSensor.ACCESS_SCREEN_USAGE -> startScreenUsageCollection()
            DeviceSensor.ACCESS_GPS -> startGpsCollection(activity)
            DeviceSensor.ACCESS_NFC -> startNfcCollection(activity)
        }

        SharedPreferencesManager.write(C.getRunningSensorKey(sensor), true)
    }

    fun stopCollectionForSensor(sensor: DeviceSensor) {
        patchSensorEntryUponFinish(sensor)
        SharedPreferencesManager.write(C.getRunningSensorKey(sensor), false)
    }

    fun updateDataCollection(sensor: DeviceSensor, status: Boolean) {
        if (status) {
            registerDataCollectionOnSensorStart(sensor)
        } else {
            registerDataCollectionOnSensorStop(sensor)
        }
    }

    fun syncDataWithCloud() {
        val asyncData = RealmManager.queryForNotSynchronizedData()
        for (data in asyncData) {
            API.sendSensorEntry(data) { sensorData ->
                sensorData?.id?.let { id ->
                    RealmManager.updateDataAfterSynchronization(id)
                }
            }
        }
    }

    private fun startWifiCollection(activity: Activity) {
        if (SettingsManager.isWifiEnabled(activity)) {
            createNewSensorEntry(DeviceSensor.ACCESS_WIFI)
        } else {
            SettingsManager.askForSensor(activity, DeviceSensor.ACCESS_WIFI)
        }
    }

    private fun startBluetoothCollection(activity: Activity) {
        if (SettingsManager.isBluetoothEnabled()) {
            createNewSensorEntry(DeviceSensor.ACCESS_BLUETOOTH)
        } else {
            SettingsManager.askForSensor(activity, DeviceSensor.ACCESS_BLUETOOTH)
        }
    }

    private fun startScreenUsageCollection() = createNewSensorEntry(DeviceSensor.ACCESS_SCREEN_USAGE)

    private fun startGpsCollection(activity: Activity) {
        if (SettingsManager.isGpsEnabled(activity)) {
            createNewSensorEntry(DeviceSensor.ACCESS_GPS)
        } else {
            SettingsManager.askForSensor(activity, DeviceSensor.ACCESS_GPS)
        }
    }

    private fun startNfcCollection(activity: Activity) {
        if (SettingsManager.isNfcEnabled(activity)) {
            createNewSensorEntry(DeviceSensor.ACCESS_NFC)
        } else {
            SettingsManager.askForSensor(activity, DeviceSensor.ACCESS_NFC)
        }
    }

    private fun createNewSensorEntry(forSensor: DeviceSensor) {
        if (RealmManager.queryForOpenEntryInSensor(forSensor)) return // Due to multiple broadcast wake

        val sensorName = C.getSensorName(forSensor)
        val sensorKey = C.getRunningSensorID(forSensor)
        val sensorData = SensorData().apply {
            this.sensorName = sensorName
        }

        val id = RealmManager.saveData(sensorData)
        SharedPreferencesManager.write(sensorKey, id)
    }

    private fun patchSensorEntryUponFinish(sensor: DeviceSensor) {
        val id = C.getRunningSensorID(sensor)
        SharedPreferencesManager.read(id, "")?.let { entryId ->
            RealmManager.updateData(entryId)
        }
    }
    
    private fun registerDataCollectionOnSensorStart(sensor: DeviceSensor) {
        val sensorKey = C.getRunningSensorKey(sensor)
        if (SharedPreferencesManager.read(sensorKey, false)) {
            createNewSensorEntry(sensor)
        }
    }

    private fun registerDataCollectionOnSensorStop(sensor: DeviceSensor){
        val sensorKey = C.getRunningSensorKey(sensor)
        if (SharedPreferencesManager.read(sensorKey, false)) {
            patchSensorEntryUponFinish(sensor)
        }
    }
}