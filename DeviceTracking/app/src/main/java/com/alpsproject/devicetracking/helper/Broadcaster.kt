package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate
import com.alpsproject.devicetracking.enums.AccessSensor

object Broadcaster {

    private val receivers: ArrayList<SensorStatusDelegate> = ArrayList()

    fun registerForBroadcasting(receiver: Activity) {
        registerForWifi(receiver)
        registerForBluetooth(receiver)
        registerForScreenUsage(receiver)

        (receiver as? SensorStatusDelegate)?.let {
            receivers.add(it)
        }
    }

    fun unregisterForBroadcasting(receiver: Activity) {
        receiver.unregisterReceiver(wifiStateReceiver)
        receiver.unregisterReceiver(bluetoothStateReceiver)
        receiver.unregisterReceiver(screenUsageReceiver)

        (receiver as? SensorStatusDelegate)?.let {
            if (receivers.contains(it)) {
                receivers.remove(it)
            }
        }
    }

    private fun registerForWifi(receiver: Activity) {
        val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        receiver.registerReceiver(wifiStateReceiver, wifiFilter)
    }

    private fun registerForBluetooth(receiver: Activity) {
        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        receiver.registerReceiver(bluetoothStateReceiver, bluetoothFilter)
    }

    private fun registerForScreenUsage(receiver: Activity) {
        val screenUsageFilter = IntentFilter()
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_OFF)
        receiver.registerReceiver(screenUsageReceiver, screenUsageFilter)
    }

    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val wifiStateExtra = it.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                val wifiOnStatus = wifiStateExtra == WifiManager.WIFI_STATE_ENABLED
                val wifiOffStatus = wifiStateExtra == WifiManager.WIFI_STATE_DISABLED

                if (wifiOnStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_WIFI, true)
                    broadcastWifiChange(true)
                } else if (wifiOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_WIFI, false)
                    broadcastWifiChange(false)
                }
            }
        }
    }

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val bluetoothStateExtra = it.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                val bluetoothOnStatus = bluetoothStateExtra == BluetoothAdapter.STATE_ON
                val bluetoothOffStatus = bluetoothStateExtra == BluetoothAdapter.STATE_OFF

                if (bluetoothOnStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_BLUETOOTH, true)
                    broadcastBluetoothChange(true)
                } else if (bluetoothOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_BLUETOOTH, false)
                    broadcastBluetoothChange(false)
                }
            }
        }
    }

    private val screenUsageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val screenStateExtra = it.action
                val screenOnStatus = screenStateExtra.equals(Intent.ACTION_SCREEN_ON)
                val screenOffStatus = screenStateExtra.equals(Intent.ACTION_SCREEN_OFF)

                if (screenOnStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_SCREEN_USAGE, true)
                    broadcastScreenStateChange(true)
                } else if (screenOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_SCREEN_USAGE, false)
                    broadcastScreenStateChange(false)
                }
            }
        }
    }

    private fun broadcastWifiChange(status: Boolean) {
        receivers.forEach {
            if (status) {
                it.didWifiEnable()
                DataCollectionManager.startRegisterDataCollection(AccessSensor.ACCESS_WIFI)
            }
            else {
                it.didWifiDisable()
                DataCollectionManager.stopRegisterDataCollection(AccessSensor.ACCESS_WIFI)
            }
        }
    }

    private fun broadcastBluetoothChange(status: Boolean) {
        receivers.forEach {
            if (status) {
                it.didBluetoothEnable()
                DataCollectionManager.startRegisterDataCollection(AccessSensor.ACCESS_BLUETOOTH)
            }
            else {
                it.didBluetoothDisable()
                DataCollectionManager.stopRegisterDataCollection(AccessSensor.ACCESS_BLUETOOTH)
            }
        }
    }

    private fun broadcastScreenStateChange(status: Boolean) {
        receivers.forEach {
            if (status) {
                it.didTurnScreenOn()
                DataCollectionManager.startRegisterDataCollection(AccessSensor.ACCESS_SCREEN_USAGE)
            }
            else {
                it.didTurnScreenOff()
                DataCollectionManager.stopRegisterDataCollection(AccessSensor.ACCESS_SCREEN_USAGE)
            }
        }
    }
}