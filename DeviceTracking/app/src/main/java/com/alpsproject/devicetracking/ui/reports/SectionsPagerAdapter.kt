package com.alpsproject.devicetracking.ui.reports

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.ui.reports.fragments.ColumnReportFragment

private val TAB_TITLES = arrayOf(
        R.string.report_tab_screen_usage,
        R.string.report_tab_wifi,
        R.string.report_tab_bluetooth
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> ColumnReportFragment.newInstance(context.getString(R.string.report_tab_screen_usage), 100)
            1 -> ColumnReportFragment.newInstance(context.getString(R.string.report_tab_wifi), 150)
            else -> ColumnReportFragment.newInstance(context.getString(R.string.report_tab_bluetooth), 123)
        }
    }

    override fun getPageTitle(position: Int): CharSequence = context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = TAB_TITLES.size

}