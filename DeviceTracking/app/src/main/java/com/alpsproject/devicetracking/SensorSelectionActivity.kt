package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

import com.alpsproject.devicetracking.views.SensorView

class SensorSelectionActivity : BaseActivity() {

    private lateinit var sensorWifiView: SensorView
    private lateinit var sensorBluetoothView: SensorView
    private lateinit var sensorScreenUsageView: SensorView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isRunning()){
            startActivity(Intent(this, DataCollectionActivity::class.java))
        }
        setContentView(R.layout.activity_sensor_selection)

        title = getString(R.string.screen_sensor_selection)
        initUI()
    }

    private fun initUI() {
        sensorWifiView = findViewById(R.id.sensor_view_wifi)
        sensorWifiView.configureSensor(getDrawable(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))

        sensorBluetoothView = findViewById(R.id.sensor_view_bluetooth)
        sensorBluetoothView.configureSensor(getDrawable(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))

        sensorScreenUsageView = findViewById(R.id.sensor_view_screen_usage)
        sensorScreenUsageView.configureSensor(getDrawable(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))

        btnNext = findViewById(R.id.btn_next_data_collection)
        btnNext.setOnClickListener {
            val selected_sensors = Intent(this, DataCollectionActivity::class.java)
            var message = "Please select at least one sensor to continue"
            var go_next = false

            if (sensorWifiView.isSensorSelected()) {
                selected_sensors.putExtra("Wifi", true)
                go_next = true
            } else {
                selected_sensors.putExtra("Wifi", false)
            }

            if (sensorBluetoothView.isSensorSelected()) {
                selected_sensors.putExtra("Bluetooth", true)
                go_next = true
            } else {
                selected_sensors.putExtra("Bluetooth", false)
            }

            if (sensorScreenUsageView.isSensorSelected()) {
                selected_sensors.putExtra("Screen Usage", true)
                go_next = true
            } else {
                selected_sensors.putExtra("Screen Usage", false)
            }

            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if(go_next) {
                startActivity(selected_sensors)
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}