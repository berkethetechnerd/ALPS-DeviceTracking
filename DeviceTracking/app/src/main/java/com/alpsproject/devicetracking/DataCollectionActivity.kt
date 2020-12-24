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
import com.alpsproject.devicetracking.helper.RealmManager
import com.alpsproject.devicetracking.helper.SettingsManager
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import com.alpsproject.devicetracking.model.SensorData
import com.alpsproject.devicetracking.views.SensorView

class DataCollectionActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var btnStartStop: Button
    private lateinit var selectedWifiView: SensorView
    private lateinit var selectedBluetoothView: SensorView
    private lateinit var selectedScreenUsageView: SensorView

    private val isWifiSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_WIFI, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_WIFI, false)
    private val isBluetoothSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_BLUETOOTH, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_BLUETOOTH, false)
    private val isScreenUsageSelected: Boolean
        get() = intent.getBooleanExtra(C.SENSOR_SCREEN_USAGE, false)
                || SharedPreferencesManager.read(C.RUNNING_SENSOR_SCREEN_USAGE, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collection)

        title = getString(R.string.data_collection_title)
        initUI()
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

        updateSelectedSensors()
    }

    private fun startStopButton() {
        if(!isRunning()) { // Starting
            SharedPreferencesManager.write(C.RUNNING_DATA_COLLECTION, true)
            SharedPreferencesManager.write(C.RUNNING_SENSOR_WIFI, isWifiSelected)
            if(isWifiSelected) {
                SettingsManager.turnWifiOn(this)

                val sensorData = SensorData()
                sensorData.sensorName = "Wifi"
                val id = RealmManager.saveData(sensorData)
                SharedPreferencesManager.write(C.RUNNING_SENSOR_WIFI_ID, id)
            }
            SharedPreferencesManager.write(C.RUNNING_SENSOR_BLUETOOTH, isBluetoothSelected)
            if(isBluetoothSelected) {
                SettingsManager.turnBluetoothOn(this)

                val sensorData = SensorData()
                sensorData.sensorName = "Bluetooth"
                val id = RealmManager.saveData(sensorData)
                SharedPreferencesManager.write(C.RUNNING_SENSOR_BLUETOOTH_ID, id)
            }
            SharedPreferencesManager.write(C.RUNNING_SENSOR_SCREEN_USAGE, isScreenUsageSelected)
            if(isScreenUsageSelected) {
                val sensorData = SensorData()
                sensorData.sensorName = "Screen Record"
                val id = RealmManager.saveData(sensorData)
                SharedPreferencesManager.write(C.RUNNING_SENSOR_SCREEN_USAGE_ID, id)
            }

            btnStartStop.text = getString(R.string.data_collection_stop)
        } else { // Stopping
            SharedPreferencesManager.write(C.RUNNING_DATA_COLLECTION, false)
            if(isWifiSelected) {
                var id = SharedPreferencesManager.read(C.RUNNING_SENSOR_WIFI_ID, "")
                id?.let {
                    RealmManager.updateData(it)
                }
            }
            SharedPreferencesManager.write(C.RUNNING_SENSOR_WIFI, false)
            if(isBluetoothSelected){
                var id = SharedPreferencesManager.read(C.RUNNING_SENSOR_BLUETOOTH_ID, "")
                id?.let {
                    RealmManager.updateData(it)
                }
            }
            SharedPreferencesManager.write(C.RUNNING_SENSOR_BLUETOOTH, false)
            if(isScreenUsageSelected){
                var id = SharedPreferencesManager.read(C.RUNNING_SENSOR_SCREEN_USAGE_ID, "")
                id?.let {
                    RealmManager.updateData(it)
                }
            }
            SharedPreferencesManager.write(C.RUNNING_SENSOR_SCREEN_USAGE, false)
            btnStartStop.text = getString(R.string.data_collection_start)

            finish()
        }
    }

    private fun updateSelectedSensors(){
        selectedWifiView = findViewById(R.id.data_collection_list_wifi)
        selectedWifiView.configureSensor(getResIcon(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))
        selectedWifiView.removeCheckBox()

        selectedBluetoothView = findViewById(R.id.data_collection_list_bluetooth)
        selectedBluetoothView.configureSensor(getResIcon(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))
        selectedBluetoothView.removeCheckBox()

        selectedScreenUsageView = findViewById(R.id.data_collection_list_screen_usage)
        selectedScreenUsageView.configureSensor(getResIcon(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))
        selectedScreenUsageView.removeCheckBox()

        if(isWifiSelected) {
            // todo: get permission and turn of sensor
        } else {
            selectedWifiView.visibility = View.GONE
        }

        if(isBluetoothSelected) {
            // todo: get permission and turn of sensor
        } else {
            selectedBluetoothView.visibility = View.GONE
        }

        if(isScreenUsageSelected) {
            // todo: get permission and turn of sensor
        } else {
            selectedScreenUsageView.visibility = View.GONE
        }
    }

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