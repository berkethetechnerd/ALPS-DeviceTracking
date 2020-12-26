package com.alpsproject.devicetracking.helper

import android.util.Log
import com.alpsproject.devicetracking.model.SensorData
import io.realm.Realm
import java.util.*

object RealmManager {

    private val realm = Realm.getDefaultInstance()

    fun saveData(sensorData: SensorData): String {
        realm.beginTransaction()
        val managedSensorData = realm.copyToRealm(sensorData) // Persist unmanaged objects
        realm.commitTransaction()

        return managedSensorData.id
    }

    fun updateData(id: String) {
        val results = realm.where(SensorData::class.java).equalTo("id", id).findAll()
        if(results.size == 0) return

        realm.beginTransaction()

        val currentResultData = results[0]
        currentResultData?.let {
            it.endTime = Date()
            realm.copyToRealmOrUpdate(it)
        }

        realm.commitTransaction()
    }

    fun printSensorData() {
        val results = realm.where(SensorData::class.java).findAll()
        for (result in results) {
            Log.d("REALM_ENTRY", result.toString())
        }
    }
}