package com.freespinslink.user.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.freespinslink.user.R


class ProgressDialog(private val mContext: Context) {

    private lateinit var progressDialog: Dialog


    fun show() {
        progressDialog = Dialog(mContext)

        progressDialog.setContentView(R.layout.custom_dialog_progress)


        val progressTv = progressDialog.findViewById<TextView>(R.id.progress_tv)
//        progressTv.text = mContext.getString(text)
        progressTv.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        progressTv.textSize = 16f

        if (progressDialog.window != null) progressDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        progressDialog.setCancelable(false)
        progressDialog.show()

    }

    fun isShowing(): Boolean {
        return if (this::progressDialog.isInitialized)
            progressDialog.isShowing
        else false
    }

    // Dismiss dialog
    fun dismiss() {
        if (!::progressDialog.isInitialized)
            progressDialog = Dialog(mContext)

//        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }

}