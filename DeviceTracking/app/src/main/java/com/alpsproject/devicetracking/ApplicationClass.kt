package com.alpsproject.devicetracking

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .name("DeviceTracker.realm")
                .build()
        )
    }
}