package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alpsproject.devicetracking.views.SensorView

class DataCollectionActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var btnStartStop: Button
    private lateinit var tvSelectedSensorTitle: TextView
    private lateinit var selectedWifiView: SensorView
    private lateinit var selectedBluetoothView: SensorView
    private lateinit var selectedScreenUsageView: SensorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collection)

        tvTitle = findViewById(R.id.tv_data_collection_title)
        tvTitle.text = getString(R.string.data_collection_title)

        btnStartStop = findViewById(R.id.btn_start_stop)
        startStopButton()

        tvSelectedSensorTitle = findViewById(R.id.tv_selected_sensor_title)
        tvSelectedSensorTitle.text = getString(R.string.selected_sensor_title)
        getSelectedSensorList()
    }

    fun startStopButton(){
        if(isRunning()){
            btnStartStop.text = getString(R.string.data_collection_stop)
        } else{
            btnStartStop.text = getString(R.string.data_collection_start)
        }
        btnStartStop.setOnClickListener {
            if(!isRunning()){
                with (sharedPref.edit()) {
                    putBoolean("RunningBackGround", true)
                    commit()
                }
                btnStartStop.text = getString(R.string.data_collection_stop)
            } else {
                with (sharedPref.edit()) {
                    putBoolean("RunningBackGround", false)
                    commit()
                }
                btnStartStop.text = getString(R.string.data_collection_start)
            }
        }
    }

    fun getSelectedSensorList(){
        selectedWifiView = findViewById(R.id.data_collection_list_wifi)
        selectedWifiView.configureSensor(getDrawable(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))
        selectedWifiView.removeCheckBox()

        selectedBluetoothView = findViewById(R.id.data_collection_list_bluetooth)
        selectedBluetoothView.configureSensor(getDrawable(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))
        selectedBluetoothView.removeCheckBox()

        selectedScreenUsageView = findViewById(R.id.data_collection_list_screen_usage)
        selectedScreenUsageView.configureSensor(getDrawable(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))
        selectedScreenUsageView.removeCheckBox()

        val selected_sensors = intent

        if( selected_sensors.getBooleanExtra("Wifi", false) ) {
            // todo: get permission and turn of sensor
        } else {
            selectedWifiView.visibility = View.GONE
        }

        if( selected_sensors.getBooleanExtra("Bluetooth", false) ) {
            // todo: get permission and turn of sensor
        } else {
            selectedBluetoothView.visibility = View.GONE
        }

        if( selected_sensors.getBooleanExtra("Screen Usage", false) ) {
            // todo: get permission and turn of sensor
        } else {
            selectedScreenUsageView.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_report, menu)
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
        if(isRunning()){
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            super.onBackPressed()
        }
    }
}