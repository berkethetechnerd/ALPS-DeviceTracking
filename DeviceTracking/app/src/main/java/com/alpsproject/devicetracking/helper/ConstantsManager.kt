package com.alpsproject.devicetracking.helper

import com.alpsproject.devicetracking.enums.DeviceSensor

object ConstantsManager {

    const val SENSOR_WIFI = "Wi-Fi"
    const val SENSOR_BLUETOOTH = "Bluetooth"
    const val SENSOR_SCREEN_USAGE = "Screen Usage"
    const val SENSOR_GPS = "Location"
    const val SENSOR_NFC = "Nfc"
    const val SENSOR_TORCH = "Torch"

    const val CONSENT_OF_USER = "ConsentForDisclaimer"
    private const val CONSENT_OF_WIFI = CONSENT_OF_USER + SENSOR_WIFI
    private const val CONSENT_OF_BLUETOOTH = CONSENT_OF_USER + SENSOR_BLUETOOTH
    private const val CONSENT_OF_SCREEN_USAGE = CONSENT_OF_USER + SENSOR_SCREEN_USAGE
    private const val CONSENT_OF_GPS = CONSENT_OF_USER + SENSOR_GPS
    private const val CONSENT_OF_NFC = CONSENT_OF_USER + SENSOR_NFC
    private const val CONSENT_OF_TORCH = CONSENT_OF_USER + SENSOR_TORCH

    const val RUNNING_DATA_COLLECTION = "RunningBackground"
    private const val RUNNING_SENSOR_WIFI = RUNNING_DATA_COLLECTION + SENSOR_WIFI
    private const val RUNNING_SENSOR_BLUETOOTH = RUNNING_DATA_COLLECTION + SENSOR_BLUETOOTH
    private const val RUNNING_SENSOR_SCREEN_USAGE = RUNNING_DATA_COLLECTION + SENSOR_SCREEN_USAGE
    private const val RUNNING_SENSOR_GPS = RUNNING_DATA_COLLECTION + SENSOR_GPS
    private const val RUNNING_SENSOR_NFC = RUNNING_DATA_COLLECTION + SENSOR_NFC
    private const val RUNNING_SENSOR_TORCH = RUNNING_DATA_COLLECTION + SENSOR_TORCH

    private const val ID = "ID"
    private const val RUNNING_SENSOR_WIFI_ID = RUNNING_SENSOR_WIFI + ID
    private const val RUNNING_SENSOR_BLUETOOTH_ID = RUNNING_SENSOR_BLUETOOTH + ID
    private const val RUNNING_SENSOR_SCREEN_USAGE_ID = RUNNING_SENSOR_SCREEN_USAGE + ID
    private const val RUNNING_SENSOR_GPS_ID = RUNNING_SENSOR_GPS + ID
    private const val RUNNING_SENSOR_NFC_ID = RUNNING_SENSOR_NFC + ID
    private const val RUNNING_SENSOR_TORCH_ID = RUNNING_SENSOR_TORCH + ID

    const val DEFAULT_API = "DEFAULT_API"
    private const val DEFAULT_API_URL = "http://192.168.0.3:8080"

    const val DEVICE_IDENTIFIER = "DEVICE_ID"
    const val DEVICE_IDENTIFIER_DEFAULT = "CORRUPTED_ID"

    fun getSensorName(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> SENSOR_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> SENSOR_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> SENSOR_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> SENSOR_GPS
            DeviceSensor.ACCESS_NFC -> SENSOR_NFC
            DeviceSensor.ACCESS_TORCH -> SENSOR_TORCH
        }
    }

    fun getRunningSensorID(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> RUNNING_SENSOR_WIFI_ID
            DeviceSensor.ACCESS_BLUETOOTH -> RUNNING_SENSOR_BLUETOOTH_ID
            DeviceSensor.ACCESS_SCREEN_USAGE -> RUNNING_SENSOR_SCREEN_USAGE_ID
            DeviceSensor.ACCESS_GPS -> RUNNING_SENSOR_GPS_ID
            DeviceSensor.ACCESS_NFC -> RUNNING_SENSOR_NFC_ID
            DeviceSensor.ACCESS_TORCH -> RUNNING_SENSOR_TORCH_ID
        }
    }

    fun getRunningSensorKey(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> RUNNING_SENSOR_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> RUNNING_SENSOR_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> RUNNING_SENSOR_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> RUNNING_SENSOR_GPS
            DeviceSensor.ACCESS_NFC -> RUNNING_SENSOR_NFC
            DeviceSensor.ACCESS_TORCH -> RUNNING_SENSOR_TORCH
        }
    }

    fun getConsentSensorKey(forSensor: DeviceSensor): String {
        return when (forSensor) {
            DeviceSensor.ACCESS_WIFI -> CONSENT_OF_WIFI
            DeviceSensor.ACCESS_BLUETOOTH -> CONSENT_OF_BLUETOOTH
            DeviceSensor.ACCESS_SCREEN_USAGE -> CONSENT_OF_SCREEN_USAGE
            DeviceSensor.ACCESS_GPS -> CONSENT_OF_GPS
            DeviceSensor.ACCESS_NFC -> CONSENT_OF_NFC
            DeviceSensor.ACCESS_TORCH -> CONSENT_OF_TORCH
        }
    }

    fun getDefaultAPIURL(): String = DEFAULT_API_URL
}