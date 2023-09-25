package com.freespinslink.user.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.freespinslink.user.R


class ProgressDialog(context: Context) :
    Dialog(context, R.style.CustomProgressDialogStyleFamily) {

    init {
        setContentView(R.layout.custom_dialog_progress)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

}