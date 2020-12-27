package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.model.SensorData
import io.realm.Realm
import java.util.Date

object RealmManager {

    private const val ENTRY_ID = "id"

    private val realm = Realm.getDefaultInstance()

    fun saveData(sensorData: SensorData): String {
        realm.beginTransaction()
        val managedSensorData = realm.copyToRealm(sensorData)
        realm.commitTransaction()

        return managedSensorData.id
    }

    fun updateData(id: String) {
        val results = realm.where(SensorData::class.java).equalTo(ENTRY_ID, id).findAll()
        if(results.size == 0) { return }

        realm.beginTransaction()

        val currentResultData = results[0]
        currentResultData?.let {
            it.endTime = Date()
            realm.copyToRealmOrUpdate(it)
        }

        realm.commitTransaction()
    }

    fun printAllData() {
        val results = realm.where(SensorData::class.java).findAll()
        results.forEach { Logger.logData(it) }
    }

    // ** QUERIES **

    fun queryForDate(stringDate: String, sensor: AccessSensor) {
        val results = realm.where(SensorData::class.java).findAll()
    }

    private fun getSensorKey(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> ConstantsManager.SENSOR_WIFI
            AccessSensor.ACCESS_BLUETOOTH -> ConstantsManager.SENSOR_BLUETOOTH
            AccessSensor.ACCESS_SCREEN_USAGE -> ConstantsManager.SENSOR_SCREEN_USAGE
        }
    }
}