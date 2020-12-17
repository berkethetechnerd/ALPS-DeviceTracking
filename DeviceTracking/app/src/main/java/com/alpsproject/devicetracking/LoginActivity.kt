package com.alpsproject.devicetracking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*

class LoginActivity : BaseActivity() {

    private lateinit var ivAlpsLogo: ImageView
    private lateinit var ivIipmLogo: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDisclamer: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ivAlpsLogo = findViewById(R.id.iv_alps_logo)
        ivIipmLogo = findViewById(R.id.iv_iipm_logo)
        tvTitle = findViewById(R.id.tv_title)
        tvDisclamer = findViewById(R.id.tv_disclaimer_text)
        btnNext = findViewById(R.id.btn_next)

        tvTitle.text = getString(R.string.login_title)
        tvDisclamer.text = getString(R.string.login_disclaimer)

        btnNext.setOnClickListener {
            val consent = sharedPref.getBoolean("ConsentOfTheUser", false)

            if(consent){
                startActivity(Intent(this, SensorSelectionActivity::class.java))
            }else{
                startActivity(Intent(this, ConsentActivity::class.java))
            }
        }
    }
}