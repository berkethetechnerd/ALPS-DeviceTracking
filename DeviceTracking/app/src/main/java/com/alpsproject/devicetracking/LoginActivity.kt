package com.alpsproject.devicetracking

import android.content.Intent
import android.os.Bundle
import android.widget.*

class LoginActivity : BaseActivity() {

    private lateinit var ivLogo: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDisclamer: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ivLogo = findViewById(R.id.iv_logo)
        tvTitle = findViewById(R.id.tv_title)
        tvDisclamer = findViewById(R.id.tv_disclaimer_text)
        btnNext = findViewById(R.id.btn_next)

        tvTitle.text = getString(R.string.login_title)
        tvDisclamer.text = getString(R.string.login_disclaimer)

        btnNext.setOnClickListener {
            startActivity(Intent(this, SensorSelectionActivity::class.java))
        }
    }
}