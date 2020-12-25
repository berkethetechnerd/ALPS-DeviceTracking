package com.alpsproject.devicetracking.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alpsproject.devicetracking.R

class SensorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var ivIcon: ImageView
    private var tvName: TextView
    private var tvStatus: TextView
    private var checkbox: CheckBox

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sensor, this, true)

        ivIcon = findViewById(R.id.iv_icon_sensor)
        tvName = findViewById(R.id.tv_name_sensor)
        tvStatus = findViewById(R.id.tv_sensor_status)
        checkbox = findViewById(R.id.cb_select_sensor)
    }

    fun configureSensor(icon: Drawable?, name: String?) {
        icon?.let {
            ivIcon.setImageDrawable(it)
        }

        name?.let {
            tvName.text = it
        }

        checkbox.isSelected = false
    }

    fun switchToStatusView() {
        tvStatus.visibility = View.VISIBLE
        checkbox.visibility = View.GONE
        sensorDisabled()
    }

    fun changeSensorStatus(enabled: Boolean) {
        if (enabled) { sensorEnabled() }
        else { sensorDisabled() }
    }

    fun deselectSensor() {
        checkbox.isChecked = false
    }

    fun isSensorSelected(): Boolean {
        return checkbox.isChecked
    }

    private fun sensorEnabled() {
        tvStatus.text = context.getString(R.string.sensor_item_enabled)
        tvStatus.setTextColor(context.getColor(R.color.green))
    }

    private fun sensorDisabled() {
        tvStatus.text = context.getString(R.string.sensor_item_disabled)
        tvStatus.setTextColor(context.getColor(R.color.red))
    }
}