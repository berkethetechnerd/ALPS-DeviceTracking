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
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode

class AllSensorReportFragment : Fragment() {

    private lateinit var usageChart: AnyChartView
    private lateinit var progressBar: ProgressBar
    private lateinit var noDataLayout: RelativeLayout
    private lateinit var tvDescription: TextView
    private lateinit var spTimeFrame: Spinner

    private lateinit var cartesian: Cartesian
    private lateinit var dataForWifi: MutableList<DataEntry>
    private lateinit var dataForBluetooth: MutableList<DataEntry>
    private lateinit var dataForScreenUsage: MutableList<DataEntry>
    private lateinit var dataForGPS: MutableList<DataEntry>
    private lateinit var dataForNFC: MutableList<DataEntry>
    private lateinit var dataForTorch: MutableList<DataEntry>

    private var selectedDay = CalendarManager.fetchCalendarDays(CalendarDays.LAST_24_HOURS)[0]
    private val dayArray = CalendarManager.fetchCalendarDays(CalendarDays.LAST_7_DAYS).reversedArray()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_sensor_report, container, false)
        initUI(view)
        initSpinner()
        initChart(isUpdate = false)
        return view
    }

    private fun initUI(view: View) {
        noDataLayout = view.findViewById(R.id.rl_all_data_no_data)
        progressBar = view.findViewById(R.id.all_sensor_progress_bar)
        tvDescription = view.findViewById(R.id.tv_all_data_report_description)

        usageChart = view.findViewById(R.id.all_sensor_usage_chart)
        usageChart.setProgressBar(progressBar)

        spTimeFrame = view.findViewById(R.id.sp_all_data_day_frame)
        spTimeFrame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDay = if (dayArray[position] == "Today") {
                    CalendarManager.fetchCalendarDays(CalendarDays.LAST_24_HOURS)[0]
                } else {
                    dayArray[position]
                }

                initChart(isUpdate = true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun initSpinner() {
        context?.let {
            dayArray[0] = "Today"
            ArrayAdapter(it, android.R.layout.simple_spinner_item, dayArray).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spTimeFrame.adapter = adapter
                spTimeFrame.setSelection(0) // Today
            }
        }
    }

    private fun initChart(isUpdate: Boolean) {
        val chartDataForWifi = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_WIFI)
        val chartDataForBluetooth = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_BLUETOOTH)
        val chartDataForScreenUsage = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_SCREEN_USAGE)
        val chartDataForGPS = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_GPS)
        val chartDataForNFC = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_NFC)
        val chartDataForTorch = RealmManager.queryForSpecificDayInSensor(selectedDay, DeviceSensor.ACCESS_TORCH)

        if (isDataExistForSelectedTimeFrame(chartDataForWifi)
                || isDataExistForSelectedTimeFrame(chartDataForBluetooth)
                || isDataExistForSelectedTimeFrame(chartDataForScreenUsage)
                || isDataExistForSelectedTimeFrame(chartDataForGPS)
                || isDataExistForSelectedTimeFrame(chartDataForNFC)
                || isDataExistForSelectedTimeFrame(chartDataForTorch)) {
            APIlib.getInstance().setActiveAnyChartView(usageChart)
            drawChart(chartDataForWifi, chartDataForBluetooth, chartDataForScreenUsage, chartDataForGPS, chartDataForNFC, chartDataForTorch, isUpdate)
            showChart()
            return
        }

        hideChart()
    }

    private fun drawChart(wifiData: DoubleArray, blData: DoubleArray, screenData: DoubleArray, gpsData: DoubleArray, nfcData: DoubleArray, torchData: DoubleArray, isUpdate: Boolean) {
        if (!isUpdate) {
            cartesian = AnyChart.line()
            dataForWifi = ArrayList()
            dataForBluetooth = ArrayList()
            dataForScreenUsage = ArrayList()
            dataForGPS = ArrayList()
            dataForNFC = ArrayList()
            dataForTorch = ArrayList()

            for (index in wifiData.indices) {
                dataForWifi.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), wifiData[index]))
                dataForBluetooth.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), blData[index]))
                dataForScreenUsage.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), screenData[index]))
                dataForGPS.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), gpsData[index]))
                dataForNFC.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), nfcData[index]))
                dataForTorch.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), torchData[index]))
            }

            val column: Line = cartesian.line(dataForWifi).name("Wifi").color(getString(R.string.sensor_color_wifi)) as Line
            column.tooltip()
                .titleFormat("Wi-Fi usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Wifi: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column2: Line = cartesian.line(dataForBluetooth).name("Bluetooth").color(getString(R.string.sensor_color_bluetooth)) as Line
            column2.tooltip()
                .titleFormat("Bluetooth usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Bluetooth: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column3: Line = cartesian.line(dataForScreenUsage).name("Screen Usage").color(getString(R.string.sensor_color_screen_usage)) as Line
            column3.tooltip()
                .titleFormat("Screen usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Screen Usage: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column4: Line = cartesian.line(dataForGPS).name("GPS").color(getString(R.string.sensor_color_gps)) as Line
            column4.tooltip()
                .titleFormat("GPS usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"GPS: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column5: Line = cartesian.line(dataForNFC).name("NFC").color(getString(R.string.sensor_color_nfc)) as Line
            column5.tooltip()
                .titleFormat("NFC usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"NFC: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column6: Line = cartesian.line(dataForTorch).name("Torch").color(getString(R.string.sensor_color_torch)) as Line
            column6.tooltip()
                .titleFormat("Torch usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Torch: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            cartesian.animation(true)
            cartesian.yScale().minimum(0.0)
            cartesian.yScale().maximum(6.0)
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.xAxis(0).title(getString(R.string.report_usage_dates))
            cartesian.yAxis(0).title(getString(R.string.report_usage_hours_total))
            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.legend()
                .enabled(true)
                .align("center")
                .itemsLayout("horizontalExpandable")
                .maxHeight(300)
                .margin(0, 0, 30, 0)
                .paginator()
                    .enabled(false)

            usageChart.setChart(cartesian)
        } else {
            if (!this::cartesian.isInitialized) {
                drawChart(wifiData, blData, screenData, gpsData, nfcData, torchData, false)
            }

            cartesian.removeAllSeries()
            dataForWifi.clear()
            dataForBluetooth.clear()
            dataForScreenUsage.clear()
            dataForGPS.clear()
            dataForNFC.clear()
            dataForTorch.clear()

            for (index in wifiData.indices) {
                dataForWifi.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), wifiData[index]))
                dataForBluetooth.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), blData[index]))
                dataForScreenUsage.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), screenData[index]))
                dataForGPS.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), gpsData[index]))
                dataForNFC.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), nfcData[index]))
                dataForTorch.add(ValueDataEntry(CalendarManager.extractHoursOfQuarterDayInString(index), torchData[index]))
            }

            val maxValue = findBestVisibleMaxValue(wifiData, blData, screenData, gpsData, nfcData, torchData)
            cartesian.yScale().maximum(maxValue)

            val column: Line = cartesian.line(dataForWifi).name("Wifi").color(getString(R.string.sensor_color_wifi)) as Line
            column.tooltip()
                .titleFormat("Wi-Fi usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Wifi: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column2: Line = cartesian.line(dataForBluetooth).name("Bluetooth").color(getString(R.string.sensor_color_bluetooth)) as Line
            column2.tooltip()
                .titleFormat("Bluetooth usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Bluetooth: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column3: Line = cartesian.line(dataForScreenUsage).name("Screen Usage").color(getString(R.string.sensor_color_screen_usage)) as Line
            column3.tooltip()
                .titleFormat("Screen usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Screen Usage: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column4: Line = cartesian.line(dataForGPS).name("GPS").color(getString(R.string.sensor_color_gps)) as Line
            column4.tooltip()
                .titleFormat("GPS usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"GPS: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column5: Line = cartesian.line(dataForNFC).name("NFC").color(getString(R.string.sensor_color_nfc)) as Line
            column5.tooltip()
                .titleFormat("NFC usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"NFC: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")

            val column6: Line = cartesian.line(dataForTorch).name("Torch").color(getString(R.string.sensor_color_torch)) as Line
            column6.tooltip()
                .titleFormat("Torch usage")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                    .format("function() {\n" +
                            "  hours = parseInt(Math.floor(this.value), 10)\n" +
                            "  minutes = parseInt(Math.floor(60 * (this.value - hours)), 10)\n" +
                            "  text = \"Torch: \"\n" +
                            "  if (hours === 0) { return text + minutes + \" minutes\" }\n" +
                            "  else if (minutes === 0) { return text + hours + \" hours\" }\n" +
                            "  else { return text + hours + \" hours \" + minutes + \" minutes\" }\n" +
                            "}")
        }
    }

    private fun findBestVisibleMaxValue(wifiData: DoubleArray, blData: DoubleArray, screenData: DoubleArray, gpsData: DoubleArray, nfcData: DoubleArray, torchData: DoubleArray): Double {
        val maxValueWifi = wifiData.maxOrNull() ?: 0.0
        val maxValueBL = blData.maxOrNull() ?: 0.0
        val maxValueScreen = screenData.maxOrNull() ?: 0.0
        val maxValueGPS = gpsData.maxOrNull() ?: 0.0
        val maxValueNFC = nfcData.maxOrNull() ?: 0.0
        val maxValueTorch = torchData.maxOrNull() ?: 0.0

        val maxValue = doubleArrayOf(maxValueWifi, maxValueBL, maxValueScreen, maxValueGPS, maxValueNFC, maxValueTorch).maxOrNull() ?: 0.0
        return if (maxValue > 4.0) {
            6.0
        } else {
            maxValue
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

    companion object {
        @JvmStatic
        fun newInstance() = AllSensorReportFragment()
    }
}