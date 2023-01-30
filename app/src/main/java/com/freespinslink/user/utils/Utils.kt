/*
 *
 * Created by Saad Iftikhar on 10/18/21, 5:19 PM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.freespinslink.user.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
    Intent(this, activity).also {
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled

    background = if (enabled) {
        ContextCompat.getDrawable(this.context, R.drawable.bg_enable)
    } else {
        ContextCompat.getDrawable(this.context, R.drawable.bg_disable)
    }
}

fun Activity.makeCall(phoneNo: String) {
    if (!TextUtils.isEmpty(phoneNo)) {

        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phoneNo")
        startActivity(Intent.createChooser(callIntent, "Make call..."))

    }
}

fun showSnackBar(view: View, message: String) {

    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

}

fun Activity.showToast(message: String) {

    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}

// Convert string to uri
fun String?.asUri(): Uri? {
    return try {
        Uri.parse(this)
    } catch (e: Exception) {
        null
    }
}

// Open uri in browser
fun String?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null

    val browserIntent = Intent(Intent.ACTION_VIEW, this.asUri())
    ContextCompat.startActivity(context, browserIntent, null)
}

fun Activity.hideKeyboard() {
    val inputManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = this.currentFocus
    if (view != null) {
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun getFormattedDate(inputDate: String): String {
    val originalFormat = SimpleDateFormat("dd-MM-yy", Locale.US)
    val targetFormat = SimpleDateFormat("dd MMM yy", Locale.US)
    val finalDate = originalFormat.parse(inputDate)
    return targetFormat.format(finalDate)
}

// Text change listener
fun AppCompatEditText.addOnTextChangedListener(onTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s.toString())
        }
    })

}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.copyToClipboard(clipLabel: String, text: CharSequence) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText(clipLabel, text))
    showToast(text.toString())
}

