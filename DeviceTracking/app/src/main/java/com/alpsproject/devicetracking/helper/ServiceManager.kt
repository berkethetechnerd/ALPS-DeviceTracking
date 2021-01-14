package com.alpsproject.devicetracking.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.alpsproject.devicetracking.enums.ServiceState
import com.alpsproject.devicetracking.services.TrackerService

object ServiceManager {

    fun startTrackerService(context: Context) {
        changeServiceState(context, ServiceState.ACTIVE)
    }

    fun stopTrackerService(context: Context) {
        changeServiceState(context, ServiceState.DISABLED)
    }

    private fun changeServiceState(context: Context, newState: ServiceState) {
        Intent(context, TrackerService::class.java).also {
            it.action = newState.toString()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, it)
            } else {
                context.startService(it)
            }
        }
    }
}