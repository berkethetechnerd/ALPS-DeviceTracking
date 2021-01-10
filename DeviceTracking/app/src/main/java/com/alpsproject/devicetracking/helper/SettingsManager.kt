package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService
import com.alpsproject.devicetracking.delegates.ActivationDelegate
import com.alpsproject.devicetracking.enums.AccessSensor


object SettingsManager: ActivationDelegate {

    init {
        UserMessageGenerator.activationDelegate = this
    }

    fun askForWiFi(activity: Activity) {
        if (PermissionManager.checkPermission(AccessSensor.ACCESS_WIFI) && !isWifiEnabled(activity)) {
            UserMessageGenerator.generateDialogForActivation(activity, AccessSensor.ACCESS_WIFI)
        }
    }

    fun askForBluetooth(activity: Activity) {
        if (PermissionManager.checkPermission(AccessSensor.ACCESS_BLUETOOTH) && !isBluetoothEnabled()) {
            UserMessageGenerator.generateDialogForActivation(
                activity,
                AccessSensor.ACCESS_BLUETOOTH
            )
        }
    }

    fun askForMobileData(activity: Activity) {
        if (PermissionManager.checkPermission(AccessSensor.ACCESS_MOBILE_DATA) && !isMobileDataEnabled(activity)) {
            UserMessageGenerator.generateDialogForActivation(activity, AccessSensor.ACCESS_MOBILE_DATA)
        }
    }

    fun askForGps(activity: Activity) {
        if (PermissionManager.checkPermission(AccessSensor.ACCESS_GPS) && !isGpsEnabled()) {
            UserMessageGenerator.generateDialogForActivation(activity, AccessSensor.ACCESS_GPS)
        }
    }

    fun isWifiEnabled(activity: Activity): Boolean {
        val wifiManager = getWifiManager(activity) ?: return false // Adapter not found
        return wifiManager.isWifiEnabled
    }

    fun isBluetoothEnabled(): Boolean = getBluetoothAdapter().isEnabled

    fun isScreenTurnedOn(activity: Activity): Boolean {
        val powerManager = getPowerManager(activity) ?: return false // Screen not found
        return powerManager.isInteractive
    }

    fun isMobileDataEnabled(activity: Activity): Boolean {
        val connectivityManager = getConnectivityManager(activity) ?: return false // Adapter not found
        val activeInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeInfo?.type == ConnectivityManager.TYPE_MOBILE
    }

    fun isGpsEnabled(): Boolean {
        // Todo: Find a way :D
        return true;
    }

    override fun sensorActivated(context: Activity, sensor: AccessSensor) {
        when (sensor) {
            AccessSensor.ACCESS_WIFI -> activateWifi(context)
            AccessSensor.ACCESS_BLUETOOTH -> activateBluetooth()
            AccessSensor.ACCESS_SCREEN_USAGE -> return
            AccessSensor.ACCESS_MOBILE_DATA -> return
            AccessSensor.ACCESS_GPS -> return
        }
    }

    private fun activateWifi(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
            activity.startActivityForResult(panelIntent, 0)
        } else {
            @Suppress("DEPRECATION")
            getWifiManager(activity)?.isWifiEnabled = true
        }
    }

    private fun activateBluetooth() = getBluetoothAdapter().enable()

    private fun getWifiManager(activity: Activity): WifiManager? = activity.applicationContext.getSystemService(
        Context.WIFI_SERVICE
    ) as? WifiManager?

    private fun getBluetoothAdapter(): BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private fun getPowerManager(activity: Activity): PowerManager? = activity.getSystemService(
        Context.POWER_SERVICE
    ) as? PowerManager?

    private fun getConnectivityManager(activity: Activity): ConnectivityManager? = activity.applicationContext.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as? ConnectivityManager?
}