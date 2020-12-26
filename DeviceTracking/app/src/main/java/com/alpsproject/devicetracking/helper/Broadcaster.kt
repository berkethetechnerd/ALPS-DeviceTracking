package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate

object Broadcaster {

    private val receivers: ArrayList<SensorStatusDelegate> = ArrayList()

    fun registerForBroadcasting(receiver: Activity) {
        val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        receiver.registerReceiver(wifiStateReceiver, wifiFilter)

        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        receiver.registerReceiver(bluetoothStateReceiver, bluetoothFilter)

        val screenUsageFilter = IntentFilter()
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_OFF)
        receiver.registerReceiver(screenUsageReceiver, screenUsageFilter)

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

    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val wifiStateExtra = it.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                val wifiStatus = wifiStateExtra == WifiManager.WIFI_STATE_ENABLED
                broadcastWifiChange(wifiStatus)
            }
        }
    }

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val bluetoothStateExtra = it.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                val bluetoothStatus = bluetoothStateExtra == BluetoothAdapter.STATE_ON
                broadcastBluetoothChange(bluetoothStatus)
            }
        }
    }

    private val screenUsageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val screenOnStatus = it.action.equals(Intent.ACTION_SCREEN_ON)
                broadcastScreenStateChange(screenOnStatus)
            }
        }
    }

    private fun broadcastWifiChange(status: Boolean) {
        for (receiver in receivers) {
            if (status) { receiver.didWifiEnable() }
            else { receiver.didWifiDisable() }
        }
    }

    private fun broadcastBluetoothChange(status: Boolean) {
        for (receiver in receivers) {
            if (status) { receiver.didBluetoothEnable() }
            else { receiver.didBluetoothDisable() }
        }
    }

    private fun broadcastScreenStateChange(status: Boolean) {
        for (receiver in receivers) {
            if (status) { receiver.didTurnScreenOn() }
            else { receiver.didTurnScreenOff() }
        }
    }
}