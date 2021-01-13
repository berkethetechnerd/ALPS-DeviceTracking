package com.alpsproject.devicetracking.helper

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate
import com.alpsproject.devicetracking.enums.AccessSensor

object Broadcaster {

    private val receivers: ArrayList<SensorStatusDelegate> = ArrayList()

    fun registerForBroadcasting(receiver: Context) {
        registerForWifi(receiver)
        registerForBluetooth(receiver)
        registerForScreenUsage(receiver)
        registerForMobileData(receiver)
        registerForGps(receiver)

        (receiver as? SensorStatusDelegate)?.let {
            receivers.add(it)
        }
    }

    fun unregisterForBroadcasting(receiver: Context) {
        receiver.unregisterReceiver(wifiStateReceiver)
        receiver.unregisterReceiver(bluetoothStateReceiver)
        receiver.unregisterReceiver(screenUsageReceiver)
        receiver.unregisterReceiver(mobileDataReceiver)
        receiver.unregisterReceiver(gpsReceiver)

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

    private fun registerForMobileData(receiver: Context) {
        val mobileDataFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        receiver.registerReceiver(mobileDataReceiver, mobileDataFilter)
    }

    private fun registerForGps(receiver: Context) {
        val gpdFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        receiver.registerReceiver(gpsReceiver, gpdFilter)
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
                    Logger.logSensorUpdate(AccessSensor.ACCESS_WIFI, true)
                    broadcastSensorChange(AccessSensor.ACCESS_WIFI, true)
                } else if (wifiOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_WIFI, false)
                    broadcastSensorChange(AccessSensor.ACCESS_WIFI, false)
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
                    Logger.logSensorUpdate(AccessSensor.ACCESS_BLUETOOTH, true)
                    broadcastSensorChange(AccessSensor.ACCESS_BLUETOOTH, true)
                } else if (bluetoothOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_BLUETOOTH, false)
                    broadcastSensorChange(AccessSensor.ACCESS_BLUETOOTH, false)
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
                    broadcastSensorChange(AccessSensor.ACCESS_SCREEN_USAGE, true)
                } else if (screenOffStatus) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_SCREEN_USAGE, false)
                    broadcastSensorChange(AccessSensor.ACCESS_SCREEN_USAGE, false)
                }
            }
        }
    }

    private val mobileDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                val noConnectivity = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
                )
                if (!noConnectivity) {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_MOBILE_DATA, true)
                    broadcastSensorChange(AccessSensor.ACCESS_MOBILE_DATA, true)
                } else {
                    Logger.logSensorUpdate(AccessSensor.ACCESS_MOBILE_DATA, false)
                    broadcastSensorChange(AccessSensor.ACCESS_MOBILE_DATA, false)
                }
            }
        }
    }

    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Logger.logSensorUpdate(AccessSensor.ACCESS_GPS, true)
                broadcastSensorChange(AccessSensor.ACCESS_GPS, true)
            } else {
                Logger.logSensorUpdate(AccessSensor.ACCESS_GPS, false)
                broadcastSensorChange(AccessSensor.ACCESS_GPS, false)
            }
        }
    }

    private fun broadcastSensorChange(sensor: AccessSensor, status: Boolean) {
        if (status) {
            DataCollectionManager.startRegisterDataCollection(sensor)
        } else {
            DataCollectionManager.stopRegisterDataCollection(sensor)
        }

        receivers.forEach {
            when (sensor) {
                AccessSensor.ACCESS_WIFI -> broadcastWifiChange(it, status)
                AccessSensor.ACCESS_BLUETOOTH -> broadcastBluetoothChange(it, status)
                AccessSensor.ACCESS_SCREEN_USAGE -> broadcastScreenStateChange(it, status)
                AccessSensor.ACCESS_MOBILE_DATA -> broadcastMobileDataChange(it, status)
                AccessSensor.ACCESS_GPS -> broadcastGpsChange(it, status)
            }
        }
    }

    private fun broadcastWifiChange(delegate: SensorStatusDelegate, status: Boolean) {
        if (status) {
            delegate.didWifiEnable()
        } else {
            delegate.didWifiDisable()
        }
    }

    private fun broadcastBluetoothChange(delegate: SensorStatusDelegate, status: Boolean) {
        if (status) {
            delegate.didBluetoothEnable()
        } else {
            delegate.didBluetoothDisable()
        }
    }

    private fun broadcastScreenStateChange(delegate: SensorStatusDelegate, status: Boolean) {
        if (status) {
            delegate.didTurnScreenOn()
        } else {
            delegate.didTurnScreenOff()
        }
    }

    private fun broadcastMobileDataChange(delegate: SensorStatusDelegate, status: Boolean) {
        if (status) {
            delegate.didMobileDataEnable()
        } else {
            delegate.didMobileDataDisable()
        }
    }

    private fun broadcastGpsChange(delegate: SensorStatusDelegate, status: Boolean) {
        if (status) {
            delegate.didGpsEnable()
        } else {
            delegate.didGpsDisable()
        }
    }

}