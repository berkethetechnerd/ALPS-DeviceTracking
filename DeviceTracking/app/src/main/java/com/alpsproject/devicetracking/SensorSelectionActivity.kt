package com.alpsproject.devicetracking

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import com.alpsproject.devicetracking.delegates.PermissionDelegate
import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.helper.PermissionManager
import com.alpsproject.devicetracking.helper.UserMessageGenerator
import com.alpsproject.devicetracking.views.SensorView

class SensorSelectionActivity : BaseActivity(), PermissionDelegate {

    private lateinit var sensorWifiView: SensorView
    private lateinit var sensorBluetoothView: SensorView
    private lateinit var sensorScreenUsageView: SensorView
    private lateinit var btnNext: Button

    private var grantedSensors: Int = 0
    private var rejectedSensors: Int = 0
    private val activeSensors: Int
        get() {
            var num = 0
            if (sensorWifiView.isSensorSelected()) num++
            if (sensorBluetoothView.isSensorSelected()) num++
            if (sensorScreenUsageView.isSensorSelected()) num++
            return num
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAlreadyRunning()
        setContentView(R.layout.activity_sensor_selection)

        UserMessageGenerator.permissionDelegate = this
        setTitle(getString(R.string.sensor_selection_title))
        initUI()
    }

    override fun onResume() {
        super.onResume()

        grantedSensors = 0
        rejectedSensors = 0
        sensorWifiView.deselectSensor()
        sensorBluetoothView.deselectSensor()
        sensorScreenUsageView.deselectSensor()
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
        btnNext.setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        grantedSensors = 0
        rejectedSensors = 0

        if (sensorScreenUsageView.isSensorSelected()) {
            if (!PermissionManager.checkPermission(AccessSensor.ACCESS_SCREEN_USAGE)) {
                PermissionManager.askPermission(this, AccessSensor.ACCESS_SCREEN_USAGE)
            } else {
                grantedSensors++
            }
        }

        if (sensorBluetoothView.isSensorSelected()) {
            if (!PermissionManager.checkPermission(AccessSensor.ACCESS_BLUETOOTH)) {
                PermissionManager.askPermission(this, AccessSensor.ACCESS_BLUETOOTH)
            } else {
                grantedSensors++
            }
        }

        if (sensorWifiView.isSensorSelected()) {
            if (!PermissionManager.checkPermission(AccessSensor.ACCESS_WIFI)) {
                PermissionManager.askPermission(this, AccessSensor.ACCESS_WIFI)
            } else {
                grantedSensors++
            }
        }

        proceedToDataCollection()
    }

    override fun permissionGranted() {
        grantedSensors++
        proceedToDataCollection()
    }

    override fun permissionRejected() {
        rejectedSensors++
        proceedToDataCollection()
    }

    private fun proceedToDataCollection() {
        if (activeSensors == 0) {
            UserMessageGenerator.generateDialogForAlert(this, getString(R.string.sensor_selection_select_at_least_one))
            return
        }

        if (activeSensors == rejectedSensors + grantedSensors) {
            if (rejectedSensors != 0) {
                UserMessageGenerator.generateDialogForAlert(this, getString(R.string.sensor_selection_select_at_least_one))
            } else {
                val selectedSensors = Intent(this, DataCollectionActivity::class.java)
                selectedSensors.putExtra(C.SENSOR_WIFI, sensorWifiView.isSensorSelected())
                selectedSensors.putExtra(C.SENSOR_BLUETOOTH, sensorBluetoothView.isSensorSelected())
                selectedSensors.putExtra(C.SENSOR_SCREEN_USAGE, sensorScreenUsageView.isSensorSelected())
                startActivity(selectedSensors)
            }
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
}