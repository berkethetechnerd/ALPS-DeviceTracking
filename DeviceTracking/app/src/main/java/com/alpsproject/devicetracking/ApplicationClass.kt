package com.alpsproject.devicetracking

import android.app.Application
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import io.realm.Realm
import io.realm.RealmConfiguration

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPreferencesManager.init(context = applicationContext)

        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .name("DeviceTracker.realm")
                .build()
        )
    }
}