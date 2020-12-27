package com.alpsproject.devicetracking.helper

import android.content.Context
import android.content.SharedPreferences
import com.alpsproject.devicetracking.R

object SharedPreferencesManager {

    private lateinit var preferences: SharedPreferences
    private val preferencesEditor: SharedPreferences.Editor
        get() = preferences.edit()

    fun init(context: Context) {
        val preferencesName = context.getString(R.string.app_name)
        preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    }

    fun read(key: String, defValue: String): String? = preferences.getString(key, defValue)
    fun read(key: String, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

    fun write(key: String, value: String) {
        with(preferencesEditor) {
            putString(key, value)
            commit()
        }
    }

    fun write(key: String, value: Boolean) {
        with(preferencesEditor) {
            putBoolean(key, value)
            commit()
        }
    }
}