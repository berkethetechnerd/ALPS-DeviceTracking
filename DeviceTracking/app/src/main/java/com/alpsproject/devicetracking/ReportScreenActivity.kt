package com.alpsproject.devicetracking

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.alpsproject.devicetracking.ui.reports.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class ReportScreenActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_screen)
        initTabLayout()
    }

    private fun initTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}