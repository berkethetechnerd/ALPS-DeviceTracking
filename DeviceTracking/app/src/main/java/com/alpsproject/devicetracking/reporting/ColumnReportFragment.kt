package com.alpsproject.devicetracking.reporting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.helper.CalendarManager
import com.alpsproject.devicetracking.helper.DummyDataGenerator
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
private const val ARG_SEED = "REPORT_SEED"

class ColumnReportFragment : Fragment() {

    private var reportName: String? = null
    private var dummyDataSeed: Long = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportName = it.getString(ARG_REPORT_TYPE)
            dummyDataSeed = it.getLong(ARG_SEED, 100)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_column_report, container, false)
        initUI(view)
        initChart(view)
        return view
    }

    private fun initUI(view: View) {
        val tvDescription: TextView = view.findViewById(R.id.tv_report_description)
        tvDescription.text = getString(R.string.report_usage_description, reportName)
    }

    private fun initChart(view: View) {
        val usageChart: AnyChartView = view.findViewById(R.id.sensor_usage_chart)
        val progressBar: ProgressBar = view.findViewById(R.id.sensor_progress_bar)
        usageChart.setProgressBar(progressBar)

        CalendarManager.last7Days()

        val cartesian: Cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()

        val dummyData = DummyDataGenerator.generateUsageHours(dummyDataSeed)
        val dummyDates = CalendarManager.last7Days()

        data.add(ValueDataEntry(dummyDates[0], dummyData[0]))
        data.add(ValueDataEntry(dummyDates[1], dummyData[1]))
        data.add(ValueDataEntry(dummyDates[2], dummyData[2]))
        data.add(ValueDataEntry(dummyDates[3], dummyData[3]))
        data.add(ValueDataEntry(dummyDates[4], dummyData[4]))
        data.add(ValueDataEntry(dummyDates[5], dummyData[5]))
        data.add(ValueDataEntry(dummyDates[6], dummyData[6]))

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
        fun newInstance(name: String, seed: Long) = ColumnReportFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPORT_TYPE, name)
                putLong(ARG_SEED, seed)
            }
        }
    }
}