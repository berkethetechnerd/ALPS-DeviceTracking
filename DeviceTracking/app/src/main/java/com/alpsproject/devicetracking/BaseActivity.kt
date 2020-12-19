package com.alpsproject.devicetracking

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alpsproject.devicetracking.helper.SharedPreferencesManager

const val RUNNING_DATA_COLLECTION = "RunningBackground"

/**
 * This activity is to be inherited by all living activities in the application
 * BaseActivity includes functions that is most common and shared by all activities.
 */

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferencesManager.init(context = applicationContext)
    }

    fun isRunning() = SharedPreferencesManager.read(RUNNING_DATA_COLLECTION, false)
    fun getResIcon(id: Int) = ContextCompat.getDrawable(applicationContext, id)
}