package com.alpsproject.devicetracking

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alpsproject.devicetracking.helper.ConstantsManager
import com.alpsproject.devicetracking.helper.SharedPreferencesManager

/**
 * This activity is to be inherited by all living activities in the application
 * BaseActivity includes functions that is most common and shared by all activities.
 */

open class BaseActivity : AppCompatActivity() {

    val C = ConstantsManager

    fun isRunning() = SharedPreferencesManager.read(C.RUNNING_DATA_COLLECTION, false)
    fun getResIcon(id: Int) = ContextCompat.getDrawable(applicationContext, id)
}