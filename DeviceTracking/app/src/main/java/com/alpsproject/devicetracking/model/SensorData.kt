package com.alpsproject.devicetracking.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class SensorData (
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var sensorName: String = "",
    var startTime: Date = Date(),
    var endTime: Date? = null
): RealmObject() {
    override fun toString(): String {
        return "$sensorName has been collected between $startTime -> $endTime"
    }
}

