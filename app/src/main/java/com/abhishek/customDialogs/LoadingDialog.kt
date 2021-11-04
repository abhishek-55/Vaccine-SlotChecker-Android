package com.abhishek.vaccineslotchecker

import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(myActivity: Activity) {
    private  val activity = myActivity
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.custom_loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}