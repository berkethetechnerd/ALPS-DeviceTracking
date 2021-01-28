package com.alpsproject.devicetracking.delegates

interface SensorStatusDelegate {
    fun didWifiEnable()
    fun didWifiDisable()
    fun didBluetoothEnable()
    fun didBluetoothDisable()
    fun didTurnScreenOn()
    fun didTurnScreenOff()
    fun didGpsEnable()
    fun didGpsDisable()
    fun didNfcEnable()
    fun didNfcDisable()
}