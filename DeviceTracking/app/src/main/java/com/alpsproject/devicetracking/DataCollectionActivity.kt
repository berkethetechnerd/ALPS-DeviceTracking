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
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import com.alpsproject.devicetracking.views.SensorView

class DataCollectionActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var btnStartStop: Button
    private lateinit var selectedWifiView: SensorView
    private lateinit var selectedBluetoothView: SensorView
    private lateinit var selectedScreenUsageView: SensorView

    private val isWifiSelected: Boolean
        get() = intent.getBooleanExtra(CONST.SENSOR_WIFI, false)
                || SharedPreferencesManager.read(CONST.RUNNING_SENSOR_WIFI, false)
    private val isBluetoothSelected: Boolean
        get() = intent.getBooleanExtra(CONST.SENSOR_BLUETOOTH, false)
                || SharedPreferencesManager.read(CONST.RUNNING_SENSOR_BLUETOOTH, false)
    private val isScreenUsageSelected: Boolean
        get() = intent.getBooleanExtra(CONST.SENSOR_SCREEN_USAGE, false)
                || SharedPreferencesManager.read(CONST.RUNNING_SENSOR_SCREEN_USAGE, false)

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
            SharedPreferencesManager.write(CONST.RUNNING_DATA_COLLECTION, true)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_WIFI, isWifiSelected)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_BLUETOOTH, isBluetoothSelected)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_SCREEN_USAGE, isScreenUsageSelected)
            btnStartStop.text = getString(R.string.data_collection_stop)
        } else { // Stopping
            SharedPreferencesManager.write(CONST.RUNNING_DATA_COLLECTION, false)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_WIFI, false)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_BLUETOOTH, false)
            SharedPreferencesManager.write(CONST.RUNNING_SENSOR_SCREEN_USAGE, false)
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
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }
}