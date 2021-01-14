package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.alpsproject.devicetracking.delegates.ActivationDelegate
import com.alpsproject.devicetracking.enums.DeviceSensor

object SettingsManager: ActivationDelegate {

    init {
        UserMessageGenerator.activationDelegate = this
    }

    fun askForSensor(activity: Activity, sensor: DeviceSensor) {
        if (PermissionManager.checkPermission(sensor)) {
            val isSensorEnabled = when (sensor) {
                DeviceSensor.ACCESS_WIFI -> isWifiEnabled(activity)
                DeviceSensor.ACCESS_BLUETOOTH -> isBluetoothEnabled()
                DeviceSensor.ACCESS_SCREEN_USAGE -> isScreenTurnedOn(activity)
                DeviceSensor.ACCESS_MOBILE_DATA -> isMobileDataEnabled(activity)
                DeviceSensor.ACCESS_GPS -> isGpsEnabled(activity)
            }

            if (!isSensorEnabled) {
                UserMessageGenerator.generateDialogForActivation(activity, sensor)
            }
        }
    }

    fun isWifiEnabled(activity: Activity): Boolean {
        val wifiManager = getWifiManager(activity) ?: return false // Adapter not found
        return wifiManager.isWifiEnabled
    }

    fun isBluetoothEnabled(): Boolean {
        val bluetoothAdapter = getBluetoothAdapter() ?: return false // Adapter not found
        return bluetoothAdapter.isEnabled
    }

    fun isScreenTurnedOn(activity: Activity): Boolean {
        val powerManager = getPowerManager(activity) ?: return false // Screen not found
        return powerManager.isInteractive
    }

    fun isMobileDataEnabled(activity: Activity): Boolean {
        val connectivityManager = getConnectivityManager(activity) ?: return false // Adapter not found
        val activeInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeInfo?.type == ConnectivityManager.TYPE_MOBILE
    }

    fun isGpsEnabled(activity: Activity): Boolean {
        val locationManager = getLocationManager(activity) ?: return false
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun sensorActivationRequested(activity: Activity, sensor: DeviceSensor) {
        when (sensor) {
            DeviceSensor.ACCESS_WIFI -> activateWifi(activity)
            DeviceSensor.ACCESS_BLUETOOTH -> activateBluetooth()
            DeviceSensor.ACCESS_SCREEN_USAGE -> return // Already on
            DeviceSensor.ACCESS_MOBILE_DATA -> activateMobileData(activity)
            DeviceSensor.ACCESS_GPS -> activateGPS(activity)
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

    private fun activateBluetooth() = getBluetoothAdapter()?.enable()

    private fun activateMobileData(context: Context) = context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))

    private fun activateGPS(context: Context) = context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

    private fun getWifiManager(activity: Activity): WifiManager? = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager?

    private fun getBluetoothAdapter(): BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private fun getPowerManager(activity: Activity): PowerManager? = activity.getSystemService(Context.POWER_SERVICE) as? PowerManager?

    private fun getConnectivityManager(activity: Activity): ConnectivityManager? = activity.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?

    fun getLocationManager(context: Context): LocationManager? = context.getSystemService(Context.LOCATION_SERVICE)as? LocationManager?
}