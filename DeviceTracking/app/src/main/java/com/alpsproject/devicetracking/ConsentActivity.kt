package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.alpsproject.devicetracking.helper.SharedPreferencesManager

class ConsentActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDisclaimer: TextView
    private lateinit var tvConsent: TextView
    private lateinit var cbConsent: CheckBox
    private lateinit var btnConsentNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)

        initUI()
    }

    private fun initUI() {
        tvTitle = findViewById(R.id.tv_title)
        tvTitle.text = getString(R.string.consent_title)

        tvDisclaimer = findViewById(R.id.tv_disclaimer_text)
        tvDisclaimer.text = getString(R.string.consent_disclaimer)

        tvConsent = findViewById(R.id.tv_consent_text)
        tvConsent.text = getString(R.string.consent_text)

        btnConsentNext = findViewById(R.id.btn_consent_next)
        btnConsentNext.setOnClickListener { proceedToSensorSelection() }

        cbConsent = findViewById(R.id.cb_consent)
    }

    private fun proceedToSensorSelection() {
        if (cbConsent.isChecked) {
            SharedPreferencesManager.write("ConsentOfTheUser", true)
            startActivity(Intent(this, SensorSelectionActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, getString(R.string.consent_must_agree), Toast.LENGTH_SHORT).show()
        }
    }
}