package com.alpsproject.devicetracking.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class SensorData (
        @SerializedName("id") @PrimaryKey var id: String = UUID.randomUUID().toString(),
        @SerializedName("sensor_name") var sensorName: String = "",
        @SerializedName("start_time") var startTime: Date = Date(),
        @SerializedName("end_time") var endTime: Date? = null,
        var isSyncWithCloud: Boolean = false
): RealmObject() {
    override fun toString(): String {
        return "$sensorName has been collected between $startTime -> $endTime and $syncText"
    }

    private val syncText: String
        get() {
            return if (this.isSyncWithCloud) "has been sent to cloud."
            else "is not sync with cloud."
        }
}

