package com.alpsproject.devicetracking.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.enums.DeviceSensor
import com.alpsproject.devicetracking.enums.CalendarDays
import com.alpsproject.devicetracking.helper.CalendarManager
import com.alpsproject.devicetracking.helper.RealmManager
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode

private const val ARG_REPORT_TYPE = "REPORT_NAME"

class ColumnReportFragment : Fragment() {

    private lateinit var usageChart: AnyChartView
    private lateinit var progressBar: ProgressBar
    private lateinit var noDataLayout: RelativeLayout
    private lateinit var tvDescription: TextView
    private lateinit var spTimeFrame: Spinner

    private lateinit var cartesian: Cartesian
    private lateinit var data: MutableList<DataEntry>

    private var reportName: String? = null
    private var timeFrame: CalendarDays = CalendarDays.LAST_3_DAYS

    private val numberOfDays: Int
        get() {
            return when(timeFrame) {
                CalendarDays.LAST_24_HOURS -> 1
                CalendarDays.LAST_3_DAYS -> 3
                CalendarDays.LAST_7_DAYS -> 7
                CalendarDays.LAST_15_DAYS -> 15
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportName = it.getString(ARG_REPORT_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_column_report, container, false)
        initUI(view)
        initSpinner()
        initChart(isUpdate = false)
        return view
    }

    private fun initUI(view: View) {
        noDataLayout = view.findViewById(R.id.rl_no_data)
        progressBar = view.findViewById(R.id.sensor_progress_bar)
        tvDescription = view.findViewById(R.id.tv_report_description)

        usageChart = view.findViewById(R.id.sensor_usage_chart)
        usageChart.setProgressBar(progressBar)

        spTimeFrame = view.findViewById(R.id.sp_time_frame)
        spTimeFrame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeFrame = when (position) {
                    0 -> CalendarDays.LAST_24_HOURS
                    1 -> CalendarDays.LAST_3_DAYS
                    2 -> CalendarDays.LAST_7_DAYS
                    else -> CalendarDays.LAST_15_DAYS
                }

                initChart(isUpdate = true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initSpinner() {
        context?.let {
            ArrayAdapter.createFromResource(it, R.array.report_time_frames, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spTimeFrame.adapter = adapter
                spTimeFrame.setSelection(1) // Last 3 days
            }
        }
    }

    private fun initChart(isUpdate: Boolean) {
        reportName?.let {
            val sensorType = getSensorType(it)
            val chartDates = CalendarManager.fetchCalendarDays(timeFrame)
            val chartData = RealmManager.queryForDatesInSensor(chartDates, sensorType, timeFrame)

            if (isDataExistForSelectedTimeFrame(chartData)) {
                tvDescription.text = getString(R.string.report_usage_description, it, numberOfDays, chartData.average())
                APIlib.getInstance().setActiveAnyChartView(usageChart)
                drawChart(chartDates, chartData, isUpdate)
                showChart()
                return
            }
        }

        tvDescription.text = getString(R.string.report_usage_description, reportName, numberOfDays, 0.0)
        hideChart()
    }

    private fun drawChart(chartDates: Array<String>, chartData: DoubleArray, isUpdate: Boolean) {
        if (!isUpdate) {
            cartesian = AnyChart.column()
            data = ArrayList()

            for (index in chartData.indices) {
                if (timeFrame == CalendarDays.LAST_24_HOURS) {
                    data.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), chartData[index]))
                } else {
                    data.add(ValueDataEntry(chartDates[index], chartData[index]))
                }
            }

            val column: Column = cartesian.column(data)
            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0.0)
                    .offsetY(5.0)
                    .format("{%Value}{groupsSeparator: } Hours")

            cartesian.animation(true)
            cartesian.yScale().minimum(0.0)
            cartesian.yScale().maximum(24.0)
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)
            cartesian.xAxis(0).title(getString(R.string.report_usage_dates))
            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
            cartesian.yAxis(0).title(getString(R.string.report_usage_hours_total))

            usageChart.setChart(cartesian)
        } else {
            cartesian.removeAllSeries()
            data.clear()

            for (index in chartData.indices) {
                if (timeFrame == CalendarDays.LAST_24_HOURS) {
                    data.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), chartData[index]))
                } else {
                    data.add(ValueDataEntry(chartDates[index], chartData[index]))
                }
            }

            if (timeFrame == CalendarDays.LAST_24_HOURS) {
                cartesian.yScale().maximum(6.0)
            } else {
                cartesian.yScale().maximum(24.0)
            }

            cartesian.column(data)
        }
    }

    private fun hideChart() {
        usageChart.visibility = View.GONE
        progressBar.visibility = View.GONE
        noDataLayout.visibility = View.VISIBLE
    }

    private fun showChart() {
        usageChart.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        noDataLayout.visibility = View.GONE
    }

    private fun isDataExistForSelectedTimeFrame(chartData: DoubleArray): Boolean {
        chartData.forEach { data ->
            if (data != 0.0) {
                return true
            }
        }

        return false
    }

    private fun getSensorType(reportName: String): DeviceSensor {
        return when(reportName) {
            getString(R.string.report_tab_wifi) -> DeviceSensor.ACCESS_WIFI
            getString(R.string.report_tab_bluetooth) -> DeviceSensor.ACCESS_BLUETOOTH
            getString(R.string.report_tab_screen_usage) -> DeviceSensor.ACCESS_SCREEN_USAGE
            getString(R.string.report_tab_gps) -> DeviceSensor.ACCESS_GPS
            getString(R.string.report_tab_nfc) -> DeviceSensor.ACCESS_NFC
            getString(R.string.report_tab_torch) -> DeviceSensor.ACCESS_TORCH
            else -> DeviceSensor.ACCESS_SCREEN_USAGE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String) = ColumnReportFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPORT_TYPE, name)
            }
        }
    }
}