package com.alpsproject.devicetracking.ui.reports.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.alpsproject.devicetracking.R
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

class BluetoothUsageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth_usage, container, false)
        initChart(view)
        return view
    }

    private fun initChart(view: View) {
        val bluetoothUsageChart: AnyChartView = view.findViewById(R.id.bluetooth_usage_chart)
        val bluetoothUsageProgress: ProgressBar = view.findViewById(R.id.bluetooth_progress_bar)
        bluetoothUsageChart.setProgressBar(bluetoothUsageProgress)

        val cartesian: Cartesian = AnyChart.column()
        val data: MutableList<DataEntry> = ArrayList()
        val dummyData = DummyDataGenerator.generateUsageHours(120)
        data.add(ValueDataEntry("15.12", dummyData[0]))
        data.add(ValueDataEntry("16.12", dummyData[1]))
        data.add(ValueDataEntry("17.12", dummyData[2]))
        data.add(ValueDataEntry("18.12", dummyData[3]))
        data.add(ValueDataEntry("19.12", dummyData[4]))
        data.add(ValueDataEntry("20.12", dummyData[5]))
        data.add(ValueDataEntry("21.12", dummyData[6]))

        val column: Column = cartesian.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%2.fValue}{groupsSeparator: } Hours")

        cartesian.animation(true)
        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        cartesian.xAxis(0).title("Dates")
        cartesian.yAxis(0).title("Hours in Total")

        bluetoothUsageChart.setChart(cartesian)
    }

    companion object {
        @JvmStatic
        fun newInstance() = BluetoothUsageFragment()
    }
}