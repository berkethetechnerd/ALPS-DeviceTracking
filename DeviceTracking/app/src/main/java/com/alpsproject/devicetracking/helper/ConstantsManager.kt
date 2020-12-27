package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.AccessSensor

object ConstantsManager {

    const val SENSOR_WIFI = "Wi-Fi"
    const val SENSOR_BLUETOOTH = "Bluetooth"
    const val SENSOR_SCREEN_USAGE = "Screen Usage"

    const val CONSENT_OF_USER = "ConsentForDisclaimer"
    const val CONSENT_OF_WIFI = CONSENT_OF_USER + SENSOR_WIFI
    const val CONSENT_OF_BLUETOOTH = CONSENT_OF_USER + SENSOR_BLUETOOTH
    const val CONSENT_OF_SCREEN_USAGE = CONSENT_OF_USER + SENSOR_SCREEN_USAGE

    const val RUNNING_DATA_COLLECTION = "RunningBackground"
    const val RUNNING_SENSOR_WIFI = RUNNING_DATA_COLLECTION + SENSOR_WIFI
    const val RUNNING_SENSOR_BLUETOOTH = RUNNING_DATA_COLLECTION + SENSOR_BLUETOOTH
    const val RUNNING_SENSOR_SCREEN_USAGE = RUNNING_DATA_COLLECTION + SENSOR_SCREEN_USAGE

    private const val ID = "ID"
    private const val RUNNING_SENSOR_WIFI_ID = RUNNING_SENSOR_WIFI + ID
    private const val RUNNING_SENSOR_BLUETOOTH_ID = RUNNING_SENSOR_BLUETOOTH + ID
    private const val RUNNING_SENSOR_SCREEN_USAGE_ID = RUNNING_SENSOR_SCREEN_USAGE + ID

    fun getSensorName(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> SENSOR_WIFI
            AccessSensor.ACCESS_BLUETOOTH -> SENSOR_BLUETOOTH
            AccessSensor.ACCESS_SCREEN_USAGE -> SENSOR_SCREEN_USAGE
        }
    }

    fun getRunningSensorKey(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> RUNNING_SENSOR_WIFI_ID
            AccessSensor.ACCESS_BLUETOOTH -> RUNNING_SENSOR_BLUETOOTH_ID
            AccessSensor.ACCESS_SCREEN_USAGE -> RUNNING_SENSOR_SCREEN_USAGE_ID
        }
    }

    fun getConsentSensorKey(forSensor: AccessSensor): String {
        return when (forSensor) {
            AccessSensor.ACCESS_WIFI -> CONSENT_OF_WIFI
            AccessSensor.ACCESS_BLUETOOTH -> CONSENT_OF_BLUETOOTH
            AccessSensor.ACCESS_SCREEN_USAGE -> CONSENT_OF_SCREEN_USAGE
        }
    }
}