package com.alpsproject.devicetracking

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
This activity is to be inherited by all living activities in the application
BaseActivity includes functions that is most common and shared by all activities.
 */
open class BaseActivity : AppCompatActivity() {

    public lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE) ?: return
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }

    fun isRunning() = sharedPref.getBoolean("RunningBackGround", false)
}