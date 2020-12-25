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
import com.alpsproject.devicetracking.helper.Broadcaster
import com.alpsproject.devicetracking.helper.DataCollectionManager
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import com.alpsproject.devicetracking.views.SensorView

class DataCollectionActivity : BaseActivity(), SensorStatusDelegate {

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

        setTitle(getString(R.string.data_collection_title))
        initUI()
        initSensors()
    }

    override fun onStart() {
        super.onStart()
        Broadcaster.registerForBroadcasting(this)
    }

    override fun onStop() {
        super.onStop()
        Broadcaster.deregisterForBroadcasting(this)
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
        selectedWifiView.configureSensor(getResIcon(R.drawable.ic_wifi_sensor), getString(R.string.sensor_wifi))
        selectedWifiView.switchToStatusView()

        selectedBluetoothView = findViewById(R.id.data_collection_list_bluetooth)
        selectedBluetoothView.configureSensor(getResIcon(R.drawable.ic_bluetooth_sensor), getString(R.string.sensor_bluetooth))
        selectedBluetoothView.switchToStatusView()

        selectedScreenUsageView = findViewById(R.id.data_collection_list_screen_usage)
        selectedScreenUsageView.configureSensor(getResIcon(R.drawable.ic_screen_usage_sensor), getString(R.string.sensor_screen_usage))
        selectedScreenUsageView.switchToStatusView()

        if(!isWifiSelected) {
            selectedWifiView.visibility = View.GONE
        }

        if(!isBluetoothSelected) {
            selectedBluetoothView.visibility = View.GONE
        }

        if(!isScreenUsageSelected) {
            selectedScreenUsageView.visibility = View.GONE
        }
    }

    private fun startStopButton() {
        fun startDataCollection() {
            SharedPreferencesManager.write(C.RUNNING_DATA_COLLECTION, true)
            btnStartStop.text = getString(R.string.data_collection_stop)

            if(isWifiSelected) { DataCollectionManager.startWifiCollection(this) }
            if(isBluetoothSelected) { DataCollectionManager.startBluetoothCollection(this) }
            if(isScreenUsageSelected) {  DataCollectionManager.startScreenUsageCollection() }
        }

        fun stopDataCollection() {
            SharedPreferencesManager.write(DataCollectionManager.C.RUNNING_DATA_COLLECTION, false)
            btnStartStop.text = getString(R.string.data_collection_start)

            if(isWifiSelected) { DataCollectionManager.stopWifiCollection() }
            if(isBluetoothSelected){ DataCollectionManager.stopBluetoothCollection() }
            if(isScreenUsageSelected){ DataCollectionManager.stopScreenUsageCollection() }
        }

        if(!isRunning()) { // Starting
            startDataCollection()
        } else { // Stopping
            stopDataCollection()
            finish()
        }
    }

    override fun didWifiEnable() {
        selectedWifiView.sensorEnabled()
    }

    override fun didWifiDisable() {
        selectedWifiView.sensorDisabled()
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