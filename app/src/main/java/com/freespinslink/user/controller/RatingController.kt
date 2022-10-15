package com.freespinslink.user.controller

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.freespinslink.user.enums.EnumRating
import com.freespinslink.user.utils.Constants
import com.freespinslink.user.utils.SharedStorage

class RatingController(val context: FragmentActivity) {

    fun showRateAppDialog() {

        AlertDialog.Builder(context)
            .setTitle("Rate the app")
            .setMessage("Would you like to rate our app?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->

                SharedStorage.setRatingState(EnumRating.DONE_RATING.value)  // Update rating state for current version
                redirectToPlayStore()

            }.setNegativeButton("No") { dialog, which ->

                SharedStorage.setRatingState(EnumRating.CANCEL_RATING.value)  // Update rating state for current version

            }.setNeutralButton("Remind me later") { dialog, which ->

                SharedStorage.setRatingState(EnumRating.RECALL_RATING.value)  // Update rating state for current version

            }.show()
    }

    // redirecting user to PlayStore
    fun redirectToPlayStore() {
        val appPackageName = context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (exception: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${Constants.PLAY_STORE_BASE_URL}$appPackageName")
                )
            )
        }
    }


}