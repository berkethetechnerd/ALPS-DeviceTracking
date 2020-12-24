package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings

object SettingsManager {

    fun turnWifiOn(activity: Activity) {
        if (PermissionManager.checkPermission(AccessPermission.ACCESS_WIFI)) {
            activateWifi(activity)
        } else {
            PermissionManager.askPermission(AccessPermission.ACCESS_WIFI)
        }
    }

    fun turnBluetoothOn() {
        if (PermissionManager.checkPermission(AccessPermission.ACCESS_BLUETOOTH)) {
            activateBluetooth()
        } else {
            PermissionManager.askPermission(AccessPermission.ACCESS_BLUETOOTH)
        }
    }

    private fun activateWifi(activity: Activity) {
        val wifiManager = getWifiManager(activity) ?: return // Adapter not found
        if (wifiManager.isWifiEnabled) {
            return // Already turned on
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            activity.startActivityForResult(panelIntent, 0)
        } else {
            @Suppress("DEPRECATION")
            wifiManager.isWifiEnabled = true
        }
    }

    private fun activateBluetooth() {
        val adapter = getBluetoothAdapter()
        adapter.enable()
    }

    private fun getWifiManager(activity: Activity): WifiManager? = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager?

    private fun getBluetoothAdapter() = BluetoothAdapter.getDefaultAdapter()
}