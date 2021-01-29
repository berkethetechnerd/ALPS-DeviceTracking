package com.alpsproject.devicetracking.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.alpsproject.devicetracking.DataCollectionActivity
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.enums.ServiceState
import com.alpsproject.devicetracking.helper.Broadcaster
import com.alpsproject.devicetracking.helper.Logger
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import com.alpsproject.devicetracking.helper.ConstantsManager as C

class TrackerService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted: Boolean = false

    private val arrOfSensors: Array<Boolean>
        get() = arrayOf(
                SharedPreferencesManager.read(C.RUNNING_SENSOR_WIFI, false),
                SharedPreferencesManager.read(C.RUNNING_SENSOR_BLUETOOTH, false),
                SharedPreferencesManager.read(C.RUNNING_SENSOR_SCREEN_USAGE, false),
                SharedPreferencesManager.read(C.RUNNING_SENSOR_GPS, false),
                SharedPreferencesManager.read(C.RUNNING_SENSOR_NFC, false)
        )

    override fun onBind(intent: Intent?): IBinder? {
        return null // We don't provide binding, so return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            Logger.logServiceNotification("Started with a null intent. It's been probably restarted by the system.")
        } else {
            when (intent.action) {
                ServiceState.ACTIVE.toString() -> startService()
                ServiceState.DISABLED.toString() -> stopService()
                else -> Logger.logServiceNotification("This should never happen. No action in the received intent")
            }
        }

        return START_STICKY // Make sure the service is restarted if the system kills the service
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
        Logger.logServiceNotification("The service has been created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Broadcaster.unregisterForBroadcasting(this, arrOfSensors)
        Logger.logServiceNotification("The service has been destroyed")
    }

    private fun startService() {
        if (isServiceStarted) return

        Logger.logServiceNotification("Starting the foreground service task")
        Broadcaster.registerForBroadcasting(this, arrOfSensors)
        Broadcaster.registerForShutdown(this)
        isServiceStarted = true

        // We need this lock so our service gets not affected by Doze Mode
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TrackerService::lock").apply {
                    acquire()
                }
            }
    }

    private fun stopService() {
        Logger.logServiceNotification("Stopping the foreground service")
        isServiceStarted = false

        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Logger.logServiceNotification("Service stopped without being started: ${e.message}")
        }
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "DEVICE TRACKER SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Device Tracker Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Device Tracker Service channel"
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, DataCollectionActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.service_description))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

}