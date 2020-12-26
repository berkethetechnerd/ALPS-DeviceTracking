package com.alpsproject.devicetracking.delegates

interface SensorStatusDelegate {
    fun didWifiEnable()
    fun didWifiDisable()
    fun didBluetoothEnable()
    fun didBluetoothDisable()
    fun didTurnScreenOn()
    fun didTurnScreenOff()
}