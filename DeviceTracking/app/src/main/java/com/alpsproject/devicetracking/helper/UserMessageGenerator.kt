package com.alpsproject.devicetracking.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

object UserMessageGenerator {

    fun generateDialogForPermission(activity: Activity, permission: AccessPermission) {
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.apply {
            setTitle("Testing dialog")
            setMessage("This is just testing for permission dialog.")
            setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                // User clicked OK
            })
            setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                // User cancelled the dialog
            })
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}