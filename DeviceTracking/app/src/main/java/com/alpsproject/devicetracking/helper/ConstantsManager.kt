package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.DeviceSensor

object ConstantsManager {

    const val SENSOR_WIFI = "Wi-Fi"
    const val SENSOR_BLUETOOTH = "Bluetooth"
    const val SENSOR_SCREEN_USAGE = "Screen Usage"
    const val SENSOR_GPS = "Location"
    const val SENSOR_NFC = "NFC"

    const val CONSENT_OF_USER = "ConsentForDisclaimer"
    private const val CONSENT_OF_WIFI = CONSENT_OF_USER + SENSOR_WIFI
    private const val CONSENT_OF_BLUETOOTH = CONSENT_OF_USER + SENSOR_BLUETOOTH
    private const val CONSENT_OF_SCREEN_USAGE = CONSENT_OF_USER + SENSOR_SCREEN_USAGE
    private const val CONSENT_OF_GPS= CONSENT_OF_USER + SENSOR_GPS
    private const val CONSENT_OF_NFC= CONSENT_OF_USER + SENSOR_NFC

    const val RUNNING_DATA_COLLECTION = "RunningBackground"
    const val RUNNING_SENSOR_WIFI = RUNNING_DATA_COLLECTION + SENSOR_WIFI
    const val RUNNING_SENSOR_BLUETOOTH = RUNNING_DATA_COLLECTION + SENSOR_BLUETOOTH
    const val RUNNING_SENSOR_SCREEN_USAGE = RUNNING_DATA_COLLECTION + SENSOR_SCREEN_USAGE
    const val RUNNING_SENSOR_GPS = RUNNING_DATA_COLLECTION + SENSOR_GPS
    const val RUNNING_SENSOR_NFC = RUNNING_DATA_COLLECTION + SENSOR_NFC

    private const val ID = "ID"
    private const val RUNNING_SENSOR_WIFI_ID = RUNNING_SENSOR_WIFI + ID
    private const val RUNNING_SENSOR_BLUETOOTH_ID = RUNNING_SENSOR_BLUETOOTH + ID
    private const val RUNNING_SENSOR_SCREEN_USAGE_ID = RUNNING_SENSOR_SCREEN_USAGE + ID
    private const val RUNNING_SENSOR_GPS_ID = RUNNING_SENSOR_GPS + ID
    private const val RUNNING_SENSOR_NFC_ID = RUNNING_SENSOR_NFC + ID

    fun getSensorName(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> SENSOR_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> SENSOR_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> SENSOR_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> SENSOR_GPS
            DeviceSensor.ACCESS_NFC -> SENSOR_NFC
        }
    }

    fun getRunningSensorID(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> RUNNING_SENSOR_WIFI_ID
            DeviceSensor.ACCESS_BLUETOOTH -> RUNNING_SENSOR_BLUETOOTH_ID
            DeviceSensor.ACCESS_SCREEN_USAGE -> RUNNING_SENSOR_SCREEN_USAGE_ID
            DeviceSensor.ACCESS_GPS -> RUNNING_SENSOR_GPS_ID
            DeviceSensor.ACCESS_NFC -> RUNNING_SENSOR_NFC_ID
        }
    }

    fun getRunningSensorKey(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> RUNNING_SENSOR_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> RUNNING_SENSOR_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> RUNNING_SENSOR_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> RUNNING_SENSOR_GPS
            DeviceSensor.ACCESS_NFC -> RUNNING_SENSOR_NFC
        }
    }

    fun getConsentSensorKey(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> CONSENT_OF_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> CONSENT_OF_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> CONSENT_OF_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> CONSENT_OF_GPS
            DeviceSensor.ACCESS_NFC -> CONSENT_OF_NFC
        }
    }
}