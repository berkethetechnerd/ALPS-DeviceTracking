package com.alpsproject.devicetracking.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.alpsproject.devicetracking.DataCollectionActivity
import com.alpsproject.devicetracking.R
import com.alpsproject.devicetracking.delegates.SensorStatusDelegate
import com.alpsproject.devicetracking.enums.ServiceState
import com.alpsproject.devicetracking.helper.Broadcaster
import com.alpsproject.devicetracking.helper.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackerService : Service(), SensorStatusDelegate {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        return null // We don't provide binding, so return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.logServiceNotification("onStartCommand executed with startId: $startId")

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
        Logger.logServiceNotification("The service has been created")
        startForeground(1, createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()
        Broadcaster.unregisterForBroadcasting(this)
        Logger.logServiceNotification("The service has been destroyed")
    }

    private fun startService() {
        if (isServiceStarted) return

        Logger.logServiceNotification("Starting the foreground service task")
        Broadcaster.registerForBroadcasting(this)
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
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
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
            .setContentTitle("Device Tracker")
            .setContentText("The service is enabled and currently collecting data.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

    override fun didWifiEnable() {
        Logger.logServiceNotification("WIFI enabled")
    }

    override fun didWifiDisable() {
        Logger.logServiceNotification("WIFI disabled")
    }

    override fun didBluetoothEnable() {
        Logger.logServiceNotification("Bluetooth enabled")
    }

    override fun didBluetoothDisable() {
        Logger.logServiceNotification("Bluetooth disabled")
    }

    override fun didTurnScreenOn() {
        Logger.logServiceNotification("Screen Usage enabled")
    }

    override fun didTurnScreenOff() {
        Logger.logServiceNotification("Screen Usage disabled")
    }

    override fun didMobileDataEnable() {
        Logger.logServiceNotification("Mobile Data enabled")
    }

    override fun didMobileDataDisnable() {
        Logger.logServiceNotification("Mobile Data disabled")
    }

}