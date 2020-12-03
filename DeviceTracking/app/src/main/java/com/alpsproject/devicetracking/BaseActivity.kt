package com.alpsproject.devicetracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
This activity is to be inherited by all living activities in the application
BaseActivity includes functions that is most common and shared by all activities.
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }
}