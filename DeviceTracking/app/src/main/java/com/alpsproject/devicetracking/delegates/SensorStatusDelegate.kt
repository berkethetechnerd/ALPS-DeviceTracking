package com.alpsproject.devicetracking.delegates

interface SensorStatusDelegate {
    fun didWifiEnable()
    fun didWifiDisable()
    fun didBluetoothEnable()
    fun didBluetoothDisable()
    fun didTurnScreenOn()
    fun didTurnScreenOff()
    fun didMobileDataEnable()
    fun didMobileDataDisable()
    fun didGpsEnable()
    fun didGpsDisable()
}