package com.alpsproject.devicetracking.helper

object Constants {

    const val CONSENT_OF_USER = "ConsentOfUser"

    const val SENSOR_WIFI = "Wi-Fi"
    const val SENSOR_BLUETOOTH = "Bluetooth"
    const val SENSOR_SCREEN_USAGE = "Screen Usage"

    const val RUNNING_DATA_COLLECTION = "RunningBackground"
    const val RUNNING_SENSOR_WIFI = RUNNING_DATA_COLLECTION + SENSOR_WIFI
    const val RUNNING_SENSOR_BLUETOOTH = RUNNING_DATA_COLLECTION + SENSOR_BLUETOOTH
    const val RUNNING_SENSOR_SCREEN_USAGE = RUNNING_DATA_COLLECTION + SENSOR_SCREEN_USAGE

}