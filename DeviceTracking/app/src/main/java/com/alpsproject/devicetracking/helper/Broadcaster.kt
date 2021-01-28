package com.alpsproject.devicetracking.helper

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate
import com.alpsproject.devicetracking.enums.DeviceSensor


object Broadcaster {

    private val receivers: ArrayList<SensorStatusDelegate> = ArrayList()

    fun registerForBroadcasting(receiver: Context, arrOfSensors: Array<Boolean>) {
        if (arrOfSensors[0]) { registerForWifi(receiver) }
        if (arrOfSensors[1]) { registerForBluetooth(receiver) }
        if (arrOfSensors[2]) { registerForScreenUsage(receiver) }
        if (arrOfSensors[3]) { registerForGps(receiver) }
        if (arrOfSensors[4]) { registerForNfc(receiver) }

        (receiver as? SensorStatusDelegate)?.let {
            receivers.add(it)
        }
    }

    fun unregisterForBroadcasting(receiver: Context, arrOfSensors: Array<Boolean>) {
        if (arrOfSensors[0]) { receiver.unregisterReceiver(wifiStateReceiver) }
        if (arrOfSensors[1]) { receiver.unregisterReceiver(bluetoothStateReceiver) }
        if (arrOfSensors[2]) { receiver.unregisterReceiver(screenUsageReceiver) }
        if (arrOfSensors[3]) { receiver.unregisterReceiver(gpsReceiver) }
        if (arrOfSensors[4]) { receiver.unregisterReceiver(nfcReceiver) }

        (receiver as? SensorStatusDelegate)?.let {
            if (receivers.contains(it)) {
                receivers.remove(it)
            }
        }
    }

    private fun registerForWifi(receiver: Context) {
        val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        receiver.registerReceiver(wifiStateReceiver, wifiFilter)
    }

    private fun registerForBluetooth(receiver: Context) {
        val bluetoothFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        receiver.registerReceiver(bluetoothStateReceiver, bluetoothFilter)
    }

    private fun registerForScreenUsage(receiver: Context) {
        val screenUsageFilter = IntentFilter()
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenUsageFilter.addAction(Intent.ACTION_SCREEN_OFF)
        receiver.registerReceiver(screenUsageReceiver, screenUsageFilter)
    }

    private fun registerForGps(receiver: Context) {
        val gpsFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        receiver.registerReceiver(gpsReceiver, gpsFilter)
    }

    private fun registerForNfc(receiver: Context) {
        val nfcFilter = IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
        receiver.registerReceiver(nfcReceiver, nfcFilter)
    }

    private val wifiStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val wifiStateExtra = it.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN
                )

                val wifiOnStatus = wifiStateExtra == WifiManager.WIFI_STATE_ENABLED
                val wifiOffStatus = wifiStateExtra == WifiManager.WIFI_STATE_DISABLED

                if (wifiOnStatus) {
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_WIFI, true)
                    broadcastSensorChange(DeviceSensor.ACCESS_WIFI, true)
                } else if (wifiOffStatus) {
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_WIFI, false)
                    broadcastSensorChange(DeviceSensor.ACCESS_WIFI, false)
                }
            }
        }
    }

    private val bluetoothStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val bluetoothStateExtra = it.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )

                val bluetoothOnStatus = bluetoothStateExtra == BluetoothAdapter.STATE_ON
                val bluetoothOffStatus = bluetoothStateExtra == BluetoothAdapter.STATE_OFF

                if (bluetoothOnStatus) {
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_BLUETOOTH, true)
                    broadcastSensorChange(DeviceSensor.ACCESS_BLUETOOTH, true)
                } else if (bluetoothOffStatus) {
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_BLUETOOTH, false)
                    broadcastSensorChange(DeviceSensor.ACCESS_BLUETOOTH, false)
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
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_SCREEN_USAGE, true)
                    broadcastSensorChange(DeviceSensor.ACCESS_SCREEN_USAGE, true)
                } else if (screenOffStatus) {
                    Logger.logSensorUpdate(DeviceSensor.ACCESS_SCREEN_USAGE, false)
                    broadcastSensorChange(DeviceSensor.ACCESS_SCREEN_USAGE, false)
                }
            }
        }
    }

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.let { ctx ->
                SettingsManager.getLocationManager(ctx)?.let { locationManager ->
                    val gpsOnStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                    if (gpsOnStatus) {
                        Logger.logSensorUpdate(DeviceSensor.ACCESS_GPS, true)
                        broadcastSensorChange(DeviceSensor.ACCESS_GPS, true)
                    } else {
                        Logger.logSensorUpdate(DeviceSensor.ACCESS_GPS, false)
                        broadcastSensorChange(DeviceSensor.ACCESS_GPS, false)
                    }
                }
            }
        }
    }

    private val nfcReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    NfcAdapter.EXTRA_ADAPTER_STATE,
                    NfcAdapter.STATE_OFF
                )
                when (state) {
                    NfcAdapter.STATE_OFF -> {
                        Logger.logSensorUpdate(DeviceSensor.ACCESS_NFC, false)
                        broadcastSensorChange(DeviceSensor.ACCESS_NFC, false)
                    }
                    NfcAdapter.STATE_ON -> {
                        Logger.logSensorUpdate(DeviceSensor.ACCESS_NFC, true)
                        broadcastSensorChange(DeviceSensor.ACCESS_NFC, true)
                    }
                }
            }
        }    }

    private fun broadcastSensorChange(sensor: DeviceSensor, status: Boolean) {
        DataCollectionManager.updateDataCollection(sensor, status)

        when (sensor) {
            DeviceSensor.ACCESS_WIFI -> broadcastWifiChange(status)
            DeviceSensor.ACCESS_BLUETOOTH -> broadcastBluetoothChange(status)
            DeviceSensor.ACCESS_SCREEN_USAGE -> broadcastScreenStateChange(status)
            DeviceSensor.ACCESS_GPS -> broadcastGpsChange(status)
            DeviceSensor.ACCESS_NFC -> broadcastNfcChange(status)
        }
    }

    private fun broadcastWifiChange(status: Boolean) {
        if (status) {
            receivers.forEach { it.didWifiEnable() }
        } else {
            receivers.forEach { it.didWifiDisable() }
        }
    }

    private fun broadcastBluetoothChange(status: Boolean) {
        if (status) {
            receivers.forEach { it.didBluetoothEnable() }
        } else {
            receivers.forEach { it.didBluetoothDisable() }
        }
    }

    private fun broadcastScreenStateChange(status: Boolean) {
        if (status) {
            receivers.forEach { it.didTurnScreenOn() }
        } else {
            receivers.forEach { it.didTurnScreenOff() }
        }
    }

    private fun broadcastGpsChange(status: Boolean) {
        if (status) {
            receivers.forEach { it.didGpsEnable() }
        } else {
            receivers.forEach { it.didGpsDisable() }
        }
    }

    private fun broadcastNfcChange(status: Boolean) {
        if (status) {
            receivers.forEach { it.didNfcEnable() }
        } else {
            receivers.forEach { it.didNfcDisable() }
        }
    }
}