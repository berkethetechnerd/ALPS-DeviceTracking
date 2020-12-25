package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate

object Broadcaster {

    private val receivers: ArrayList<SensorStatusDelegate> = ArrayList()

    fun registerForBroadcasting(receiver: Activity) {
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        receiver.registerReceiver(wifiStateReceiver, intentFilter)

        (receiver as? SensorStatusDelegate)?.let {
            receivers.add(it)
        }
    }

    fun deregisterForBroadcasting(receiver: Activity) {
        receiver.unregisterReceiver(wifiStateReceiver)

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

    private fun broadcastWifiChange(status: Boolean) {
        for (receiver in receivers) {
            if (status) { receiver.didWifiEnable() }
            else { receiver.didWifiDisable() }
        }
    }
}