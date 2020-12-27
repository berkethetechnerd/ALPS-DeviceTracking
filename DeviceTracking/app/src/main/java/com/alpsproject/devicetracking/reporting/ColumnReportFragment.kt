package com.alpsproject.devicetracking.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
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

    private var reportName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportName = it.getString(ARG_REPORT_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_column_report, container, false)
        initUI(view)
        initChart(view)
        return view
    }

    private fun initUI(view: View) {
        progressBar = view.findViewById(R.id.sensor_progress_bar)
        noDataLayout = view.findViewById(R.id.rl_no_data)

        usageChart = view.findViewById(R.id.sensor_usage_chart)
        usageChart.setProgressBar(progressBar)

        tvDescription = view.findViewById(R.id.tv_report_description)
        tvDescription.text = getString(R.string.report_usage_description, reportName)
    }

    private fun initChart(view: View) {
        // TODO: Check for data existence, if null show no data!
        noDataLayout.visibility = View.GONE

        // TODO: Check for respective sensor

        val cartesian: Cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()

        val chartDates = CalendarManager.fetchCalendarDays(CalendarDays.LAST_7_DAYS)
        val chartData = RealmManager.queryForDatesInSensor(chartDates, AccessSensor.ACCESS_WIFI)

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

    companion object {
        @JvmStatic
        fun newInstance(name: String) = ColumnReportFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPORT_TYPE, name)
            }
        }
    }
}