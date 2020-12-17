package com.alpsproject.devicetracking.ui.reports.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alpsproject.devicetracking.R

class WifiUsageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wifi_usage, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = WifiUsageFragment()
    }
}