package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.alpsproject.devicetracking.helper.SharedPreferencesManager

class LoginActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDisclaimer: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
    }

    private fun initUI() {
        tvTitle = findViewById(R.id.tv_title)
        tvTitle.text = getString(R.string.login_title)

        tvDisclaimer = findViewById(R.id.tv_disclaimer_text)
        tvDisclaimer.text = getString(R.string.login_disclaimer)

        btnNext = findViewById(R.id.btn_next)
        btnNext.setOnClickListener { proceedToApp() }
    }

    private fun proceedToApp() {
        val isConsentGiven = SharedPreferencesManager.read(C.CONSENT_OF_USER, false)

        if (isConsentGiven) {
            startActivity(Intent(this, SensorSelectionActivity::class.java))
        } else {
            startActivity(Intent(this, ConsentActivity::class.java))
        }
    }
}