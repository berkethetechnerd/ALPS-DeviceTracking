package com.alpsproject.devicetracking.reporting

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alpsproject.devicetracking.R

private val TAB_TITLES = arrayOf(
        R.string.report_tab_screen_usage,
        R.string.report_tab_wifi,
        R.string.report_tab_bluetooth,
        R.string.report_tab_mobile_data,
        R.string.report_tab_gps
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = ColumnReportFragment.newInstance(context.getString(TAB_TITLES[position]))

    override fun getPageTitle(position: Int): CharSequence = context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = TAB_TITLES.size

}