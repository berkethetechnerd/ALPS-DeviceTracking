package com.alpsproject.devicetracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ConsentActivity : BaseActivity() {
    private lateinit var tvTitle: TextView
    private lateinit var tvDisclaimer: TextView
    private lateinit var tvConsent: TextView
    private lateinit var cbConsent: CheckBox
    private lateinit var btnConsentNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consent)

        tvTitle = findViewById(R.id.tv_title)
        tvDisclaimer = findViewById(R.id.tv_disclaimer_text)
        tvConsent = findViewById(R.id.tv_consent_text)
        cbConsent = findViewById(R.id.cb_consent)
        btnConsentNext = findViewById(R.id.btn_consent_next)

        tvTitle.text = getString(R.string.consent_title)
        tvDisclaimer.text = getString(R.string.consent_disclaimer)
        tvConsent.text = getString(R.string.consent_text)

        var message = "You need to give consent before participating"

        btnConsentNext.setOnClickListener {
            if(cbConsent.isChecked){
                val sharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE) ?: return@setOnClickListener
                with (sharedPref.edit()) {
                    putBoolean("ConsentOfTheUser", true)
                    commit()
                }
                startActivity(Intent(this, SensorSelectionActivity::class.java))
                finish()
            } else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}