package com.alpsproject.devicetracking.ui.reports

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.ui.reports.fragments.BluetoothUsageFragment
import com.alpsproject.devicetracking.ui.reports.fragments.ScreenUsageFragment
import com.alpsproject.devicetracking.ui.reports.fragments.WifiUsageFragment

private val TAB_TITLES = arrayOf(
        R.string.report_tab_screen_usage,
        R.string.report_tab_wifi,
        R.string.report_tab_bluetooth
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ScreenUsageFragment.newInstance()
            1 -> WifiUsageFragment.newInstance()
            else -> BluetoothUsageFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}