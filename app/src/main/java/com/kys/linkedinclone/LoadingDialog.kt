package com.kys.linkedinclone

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(val activity: Activity?) {
    private lateinit var dialog: AlertDialog

    @SuppressLint("InflateParams")
    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.progressdialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        this.dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}
