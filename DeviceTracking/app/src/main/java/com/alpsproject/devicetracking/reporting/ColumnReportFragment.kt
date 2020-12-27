package com.alpsproject.devicetracking.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.enums.AccessSensor
import com.alpsproject.devicetracking.enums.CalendarDays
import com.alpsproject.devicetracking.helper.CalendarManager
import com.alpsproject.devicetracking.helper.RealmManager
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

    private var reportName: String? = null
    private var timeFrame: CalendarDays = CalendarDays.LAST_7_DAYS

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
        initChart()
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
                // TODO: Implement this function : Change average, day number of description. Update chart!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO: Implement this function
            }
        }
    }

    private fun initSpinner() {
        context?.let {
            ArrayAdapter.createFromResource(it, R.array.report_time_frames, android.R.layout.simple_spinner_item).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spTimeFrame.adapter = adapter
                spTimeFrame.setSelection(1) // Last 7 days
            }
        }
    }

    private fun initChart() {
        reportName?.let {
            val sensorType = getSensorType(it)
            val chartDates = CalendarManager.fetchCalendarDays(timeFrame)
            val chartData = RealmManager.queryForDatesInSensor(chartDates, sensorType)

            if (isDataExistForSelectedTimeFrame(chartData)) {
                tvDescription.text = getString(R.string.report_usage_description, it, chartData.average())
                drawChart(chartDates, chartData)
                return
            }
        }

        tvDescription.text = getString(R.string.report_usage_description, reportName, 0.0)
        hideChart()
    }

    private fun drawChart(chartDates: Array<String>, chartData: DoubleArray) {
        val cartesian: Cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()

        for (index in chartData.indices) {
            data.add(ValueDataEntry(chartDates[index], chartData[index]))
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
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title(getString(R.string.report_usage_dates))
        cartesian.yAxis(0).title(getString(R.string.report_usage_hours_total))

        usageChart.setChart(cartesian)
    }

    private fun hideChart() {
        usageChart.visibility = View.GONE
        progressBar.visibility = View.GONE
        noDataLayout.visibility = View.VISIBLE
    }

    private fun isDataExistForSelectedTimeFrame(chartData: DoubleArray): Boolean {
        chartData.forEach { data ->
            if (data != 0.0) {
                return true
            }
        }

        return false
    }

    private fun getSensorType(reportName: String): AccessSensor {
        return when(reportName) {
            getString(R.string.report_tab_screen_usage) -> AccessSensor.ACCESS_SCREEN_USAGE
            getString(R.string.report_tab_wifi) -> AccessSensor.ACCESS_WIFI
            else -> AccessSensor.ACCESS_BLUETOOTH
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