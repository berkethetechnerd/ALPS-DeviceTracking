package com.alpsproject.devicetracking

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import com.alpsproject.devicetracking.helper.DataCollectionManager
import com.alpsproject.devicetracking.helper.Logger
import com.alpsproject.devicetracking.helper.RealmManager
import com.alpsproject.devicetracking.helper.SharedPreferencesManager
import java.util.*

class LoginActivity : BaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDisclaimer: TextView
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initUI()
        initDeviceUniqueIdentifier()

        RealmManager.printAllData()
        DataCollectionManager.syncDataWithCloud()
    }

    private fun initUI() {
        tvTitle = findViewById(R.id.tv_title)
        tvTitle.text = getString(R.string.login_title)

        tvDisclaimer = findViewById(R.id.tv_disclaimer_text)
        tvDisclaimer.text = getString(R.string.login_disclaimer)

        btnNext = findViewById(R.id.btn_next)
        btnNext.setOnClickListener { proceedToApp() }
    }

    private fun initDeviceUniqueIdentifier() {
        val oldIdentifier = SharedPreferencesManager.read(C.DEVICE_IDENTIFIER, "")
        if (oldIdentifier.isNullOrEmpty()) {
            val newIdentifier = UUID.randomUUID().toString()
            Logger.logUniqueIdentifier(newIdentifier, true)
            SharedPreferencesManager.write(C.DEVICE_IDENTIFIER, newIdentifier)
            return
        }

        Logger.logUniqueIdentifier(oldIdentifier, false)
    }

    private fun proceedToApp() {
        if (SharedPreferencesManager.read(C.CONSENT_OF_USER, false)) {
            startActivity(Intent(this, SensorSelectionActivity::class.java))
        } else {
            startActivity(Intent(this, ConsentActivity::class.java))
        }
    }

    private fun generateDialogForAPICustomization() {
        val inputTextField = EditText(this)
        val apiAddress = SharedPreferencesManager.read(C.DEFAULT_API, C.getDefaultAPIURL())
        inputTextField.setText(apiAddress)

        val dialog = AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_for_api_endpoint_message))
                .setView(inputTextField)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_for_api_endpoint_save)) { dialogInterface, _ ->
                    val value = inputTextField.text.toString()
                    SharedPreferencesManager.write(C.DEFAULT_API, value)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(getString(R.string.dialog_for_api_endpoint_default)) { dialogInterface, _ ->
                    SharedPreferencesManager.write(C.DEFAULT_API, C.getDefaultAPIURL())
                    inputTextField.setText(C.getDefaultAPIURL())
                    dialogInterface.dismiss()
                }
                .create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)

        // Just coloring the icon
        val iconDrawable: Drawable = menu.findItem(R.id.menu_settings).icon
        iconDrawable.mutate()
        iconDrawable.setTint(getColor(R.color.white))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            generateDialogForAPICustomization()
            return true
        }

        return false
    }
}