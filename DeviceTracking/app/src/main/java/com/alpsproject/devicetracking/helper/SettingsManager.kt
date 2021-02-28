package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.alpsproject.devicetracking.delegates.ActivationDelegate
import com.alpsproject.devicetracking.enums.DeviceSensor

object SettingsManager: ActivationDelegate {

    private var isTorchEnabled = false

    init {
        UserMessageGenerator.activationDelegate = this
    }

    fun askForSensor(activity: Activity, sensor: DeviceSensor) {
        if (PermissionManager.checkPermission(sensor)) {
            val isSensorEnabled = when (sensor) {
                DeviceSensor.ACCESS_WIFI -> isWifiEnabled(activity)
                DeviceSensor.ACCESS_BLUETOOTH -> isBluetoothEnabled()
                DeviceSensor.ACCESS_SCREEN_USAGE -> isScreenTurnedOn(activity)
                DeviceSensor.ACCESS_GPS -> isGpsEnabled(activity)
                DeviceSensor.ACCESS_NFC -> isNfcEnabled(activity)
                DeviceSensor.ACCESS_TORCH -> isTorchEnabled()
            }

            if (!isSensorEnabled) {
                // Disabled feature by request
                // UserMessageGenerator.generateDialogForActivation(activity, sensor)
                return
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

    fun isGpsEnabled(activity: Activity): Boolean {
        val locationManager = getLocationManager(activity) ?: return false // Provider not found
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isNfcEnabled(activity: Activity): Boolean {
        val nfcManager = getNfcManager(activity) ?: return false // Adapter not found
        return nfcManager.defaultAdapter.isEnabled
    }

    fun isTorchEnabled(): Boolean {
        return this.isTorchEnabled
    }

    fun setTorchStatus(enabled: Boolean) {
        this.isTorchEnabled = enabled
    }

    override fun sensorActivationRequested(activity: Activity, sensor: DeviceSensor) {
        when (sensor) {
            DeviceSensor.ACCESS_WIFI -> activateWifi(activity)
            DeviceSensor.ACCESS_BLUETOOTH -> activateBluetooth()
            DeviceSensor.ACCESS_SCREEN_USAGE -> return // Already on
            DeviceSensor.ACCESS_GPS -> activateGPS(activity)
            DeviceSensor.ACCESS_NFC -> activateNFC(activity)
            DeviceSensor.ACCESS_TORCH -> activateTorch(activity)
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

    private fun activateGPS(context: Context) = context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

    private fun activateNFC(context: Context) = context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))

    private fun activateTorch(context: Context) = getCameraManager(context)?.setTorchMode("0", true)

    private fun getWifiManager(activity: Activity): WifiManager? = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager?

    private fun getBluetoothAdapter(): BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private fun getPowerManager(activity: Activity): PowerManager? = activity.getSystemService(Context.POWER_SERVICE) as? PowerManager?

    fun getLocationManager(context: Context): LocationManager? = context.getSystemService(Context.LOCATION_SERVICE)as? LocationManager?

    private fun getNfcManager(context: Context): NfcManager? = context.getSystemService(Context.NFC_SERVICE)as? NfcManager?

    fun getCameraManager(context: Context): CameraManager? = context.getSystemService(Context.CAMERA_SERVICE)as? CameraManager?
}