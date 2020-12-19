package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.alpsproject.devicetracking.views.SensorView

const val SENSOR_WIFI = "Wi-Fi"
const val SENSOR_BLUETOOTH = "Bluetooth"
const val SENSOR_SCREEN_USAGE = "Screen Usage"

class SensorSelectionActivity : BaseActivity() {

    private lateinit var sensorWifiView: SensorView
    private lateinit var sensorBluetoothView: SensorView
    private lateinit var sensorScreenUsageView: SensorView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAlreadyRunning()
        setContentView(R.layout.activity_sensor_selection)

        title = getString(R.string.sensor_selection_title)
        initUI()
    }

    private fun checkIfAlreadyRunning() {
        if (isRunning()) {
            startActivity(Intent(this, DataCollectionActivity::class.java))
        }
    }

    private fun initUI() {
        sensorWifiView = findViewById(R.id.sensor_view_wifi)
        sensorWifiView.configureSensor(getResIcon(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))

        sensorBluetoothView = findViewById(R.id.sensor_view_bluetooth)
        sensorBluetoothView.configureSensor(getResIcon(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))

        sensorScreenUsageView = findViewById(R.id.sensor_view_screen_usage)
        sensorScreenUsageView.configureSensor(getResIcon(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))

        btnNext = findViewById(R.id.btn_next_data_collection)
        btnNext.setOnClickListener { proceedToDataCollection() }
    }

    private fun proceedToDataCollection() {
        val selectedSensors = Intent(this, DataCollectionActivity::class.java)
        selectedSensors.putExtra(SENSOR_WIFI, sensorWifiView.isSensorSelected())
        selectedSensors.putExtra(SENSOR_BLUETOOTH, sensorBluetoothView.isSensorSelected())
        selectedSensors.putExtra(SENSOR_SCREEN_USAGE, sensorScreenUsageView.isSensorSelected())

        if (isAnySensorSelected()) {
            startActivity(selectedSensors)
        } else {
            Toast.makeText(this, getString(R.string.sensor_selection_select_at_least_one), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAnySensorSelected() = sensorWifiView.isSensorSelected()
            || sensorBluetoothView.isSensorSelected()
            || sensorScreenUsageView.isSensorSelected()
}