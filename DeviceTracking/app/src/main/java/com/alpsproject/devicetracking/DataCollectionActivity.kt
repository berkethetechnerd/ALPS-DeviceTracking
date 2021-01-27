package com.alpsproject.devicetracking

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.helper.*
import com.alpsproject.devicetracking.views.SensorView

class DataCollectionActivity : BaseActivity(), SensorStatusDelegate {

    private lateinit var tvTitle: TextView
    private lateinit var btnStartStop: Button
    private lateinit var selectedWifiView: SensorView
    private lateinit var selectedBluetoothView: SensorView
    private lateinit var selectedScreenUsageView: SensorView
    private lateinit var selectedGpsView: SensorView

    private val isWifiSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_WIFI, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_WIFI, false)
    private val isBluetoothSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_BLUETOOTH, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_BLUETOOTH, false)
    private val isScreenUsageSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_SCREEN_USAGE, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_SCREEN_USAGE, false)
    private val isGpsSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_GPS, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_GPS, false)

    private val arrOfSelectedSensors: Array<Boolean>
        get() = arrayOf(
                isWifiSelected,
                isBluetoothSelected,
                isScreenUsageSelected,
                isGpsSelected
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collection)

        setTitle(getString(R.string.data_collection_title))
        initUI()
        initSensors()
    }

    override fun onStart() {
        super.onStart()
        Broadcaster.registerForBroadcasting(this, arrOfSelectedSensors)
    }

    override fun onDestroy() {
        super.onDestroy()
        Broadcaster.unregisterForBroadcasting(this, arrOfSelectedSensors)
    }

    private fun initUI() {
        tvTitle = findViewById(R.id.tv_data_collection_title)
        tvTitle.text = getString(R.string.data_collection_title)

        btnStartStop = findViewById(R.id.btn_start_stop)
        btnStartStop.setOnClickListener { startStopButton() }

        if (isRunning()) {
            btnStartStop.text = getString(R.string.data_collection_stop)
        } else {
            btnStartStop.text = getString(R.string.data_collection_start)
        }
    }

    private fun initSensors(){
        selectedWifiView = findViewById(R.id.data_collection_list_wifi)
        selectedBluetoothView = findViewById(R.id.data_collection_list_bluetooth)
        selectedScreenUsageView = findViewById(R.id.data_collection_list_screen_usage)
        selectedGpsView = findViewById(R.id.data_collection_list_gps)

        if(!isWifiSelected) {
            selectedWifiView.visibility = View.GONE
        } else {
            selectedWifiView.switchToStatusView()
            selectedWifiView.configureSensor(getResIcon(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))
            selectedWifiView.changeSensorStatus(SettingsManager.isWifiEnabled(this))
        }

        if(!isBluetoothSelected) {
            selectedBluetoothView.visibility = View.GONE
        } else {
            selectedBluetoothView.switchToStatusView()
            selectedBluetoothView.configureSensor(getResIcon(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))
            selectedBluetoothView.changeSensorStatus(SettingsManager.isBluetoothEnabled())
        }

        if(!isScreenUsageSelected) {
            selectedScreenUsageView.visibility = View.GONE
        } else {
            selectedScreenUsageView.switchToStatusView()
            selectedScreenUsageView.configureSensor(getResIcon(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))
            selectedScreenUsageView.changeSensorStatus(SettingsManager.isScreenTurnedOn(this))
        }

        if(!isGpsSelected) {
            selectedGpsView.visibility = View.GONE
        } else {
            selectedGpsView.switchToStatusView()
            selectedGpsView.configureSensor(getResIcon(R.drawable.ic_gps_sensor), getString(R.string.sensor_gps))
            selectedGpsView.changeSensorStatus(SettingsManager.isGpsEnabled(this))
        }
    }

    private fun startStopButton() {
        fun startDataCollection() {
            SharedPreferencesManager.write(C.RUNNING_DATA_COLLECTION, true)
            btnStartStop.text = getString(R.string.data_collection_stop)

            if(isWifiSelected) { DataCollectionManager.startCollectionForSensor(DeviceSensor.ACCESS_WIFI, this) }
            if(isBluetoothSelected) { DataCollectionManager.startCollectionForSensor(DeviceSensor.ACCESS_BLUETOOTH, this) }
            if(isScreenUsageSelected) {  DataCollectionManager.startCollectionForSensor(DeviceSensor.ACCESS_SCREEN_USAGE, this) }
            if(isGpsSelected) { DataCollectionManager.startCollectionForSensor(DeviceSensor.ACCESS_GPS, this) }
        }

        fun stopDataCollection() {
            SharedPreferencesManager.write(C.RUNNING_DATA_COLLECTION, false)
            btnStartStop.text = getString(R.string.data_collection_start)

            if(isWifiSelected) { DataCollectionManager.stopCollectionForSensor(DeviceSensor.ACCESS_WIFI) }
            if(isBluetoothSelected){ DataCollectionManager.stopCollectionForSensor(DeviceSensor.ACCESS_BLUETOOTH) }
            if(isScreenUsageSelected){ DataCollectionManager.stopCollectionForSensor(DeviceSensor.ACCESS_SCREEN_USAGE) }
            if(isGpsSelected) { DataCollectionManager.stopCollectionForSensor(DeviceSensor.ACCESS_GPS) }
        }

        if(!isRunning()) { // Starting
            startDataCollection()
            ServiceManager.startTrackerService(this)
        } else { // Stopping
            stopDataCollection()
            ServiceManager.stopTrackerService(this)
            DataCollectionManager.syncDataWithCloud()
            finish()
        }
    }

    override fun didWifiEnable() = selectedWifiView.changeSensorStatus(true)

    override fun didWifiDisable() = selectedWifiView.changeSensorStatus(false)

    override fun didBluetoothEnable() = selectedBluetoothView.changeSensorStatus(true)

    override fun didBluetoothDisable() = selectedBluetoothView.changeSensorStatus(false)

    override fun didTurnScreenOn() = selectedScreenUsageView.changeSensorStatus(true)

    override fun didTurnScreenOff() = selectedScreenUsageView.changeSensorStatus(false)

    override fun didGpsEnable() = selectedGpsView.changeSensorStatus(true)

    override fun didGpsDisable() = selectedGpsView.changeSensorStatus(false)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_report, menu)

        // Just coloring the icon
        val iconDrawable: Drawable = menu.findItem(R.id.menu_report_screen).icon
        iconDrawable.mutate()
        iconDrawable.setTint(getColor(R.color.white))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_report_screen) {
            startActivity(Intent(this, ReportScreenActivity::class.java))
            return true
        }

        return false
    }

    override fun onBackPressed() {
        if (isRunning()) {
            val launcherIntent = Intent(this, LoginActivity::class.java)
            launcherIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(launcherIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}