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
    private var checkbox: CheckBox

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sensor, this, true)

        ivIcon = findViewById(R.id.iv_icon_sensor)
        tvName = findViewById(R.id.tv_name_sensor)
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

    fun isSensorSelected(): Boolean {
        return checkbox.isChecked
    }

    fun removeCheckBox() {
        checkbox.visibility = View.GONE
    }
}