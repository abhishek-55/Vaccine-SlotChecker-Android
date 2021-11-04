package com.abhishek.vaccineslotchecker

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface


class NoSessionDialog(myActivity: Activity) {
    private  val activity = myActivity
    private lateinit var dialog: AlertDialog

    fun startDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.no_sessions_dialog, null))
        builder.setCancelable(true)
        builder.setPositiveButton("Okay. "){ _ : DialogInterface, _ : Int ->
            dialog.dismiss()
        }

        dialog = builder.create()
        dialog.show()
    }
}